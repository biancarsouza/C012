import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

class Carro extends Thread {
    private String nome;
    private int prioridade; // Prioridade do carro (menor = mais prioridade)
    private Semaforo semaforo1;
    private Semaforo semaforo2;

    private long tempoInicio;
    private long tempoFim;

    private static List<Carro> carrosFinalizados = new ArrayList<>();
    private static final Object lock = new Object();

    // Fila com ordenação por prioridade
    private static Queue<Carro> filaSemaforo = new PriorityQueue<>(new Comparator<Carro>() {
        public int compare(Carro c1, Carro c2) {
            return Integer.compare(c1.prioridade, c2.prioridade);
        }
    });

    private static int qtdCarros = 0;

    public Carro(String nome, int prioridade, Semaforo semaforo1, Semaforo semaforo2) {
        this.nome = nome;
        this.prioridade = prioridade;
        this.semaforo1 = semaforo1;
        this.semaforo2 = semaforo2;
    }

    public void run() {
        tempoInicio = System.currentTimeMillis(); // Marca o início do percurso

        try {
            synchronized (lock) {
                filaSemaforo.add(this);
                System.out.println(nome + " (Prioridade " + prioridade + ") entrou na FILA do PRIMEIRO semáforo.");
            }

            while (true) {
                synchronized (lock) {
                    Carro primeiro = filaSemaforo.peek();
                    if (primeiro == this && semaforo1.atravessar()) {
                        filaSemaforo.poll();
                        System.out.println(nome + " está ATRAVESSANDO o PRIMEIRO semáforo.");
                        Thread.sleep(2000);
                        System.out.println(nome + " PASSOU pelo PRIMEIRO semáforo!");
                        break;
                    }
                }
                Thread.sleep(1000);
            }

            Thread.sleep(2000);

            synchronized (lock) {
                filaSemaforo.add(this);
                List<Carro> ordenada = new ArrayList<>(filaSemaforo);
                ordenada.sort(Comparator.comparingInt(c -> c.prioridade));
                filaSemaforo = new LinkedList<>(ordenada);

                System.out.println(nome + " (Prioridade " + prioridade + ") entrou na FILA do SEGUNDO semáforo.");
            }

            while (true) {
                synchronized (lock) {
                    Carro primeiro = filaSemaforo.peek();
                    if (primeiro == this && semaforo2.atravessar()) {
                        filaSemaforo.poll();
                        System.out.println(nome + " está ATRAVESSANDO o SEGUNDO semáforo.");
                        Thread.sleep(2000);

                        tempoFim = System.currentTimeMillis();
                        long duracao = (tempoFim - tempoInicio) / 1000; // segundos

                        System.out.println(nome + " PASSOU pelo SEGUNDO semáforo e FINALIZOU o percurso em " + duracao + " segundos!");

                        carrosFinalizados.add(this);
                        qtdCarros++;

                        if (qtdCarros == 5) {
                            System.out.println("\n=== TODOS OS CARROS FINALIZARAM ===");

                            System.out.println("\nOrdem de término:");
                            for (int i = 0; i < carrosFinalizados.size(); i++) {
                                Carro c = carrosFinalizados.get(i);
                                long t = (c.tempoFim - c.tempoInicio) / 1000;
                                System.out.println((i + 1) + "º - " + c.nome + " (Prioridade " + c.prioridade + ") - Tempo: " + t + "s");
                            }

                            System.out.println("\nOrdem de prioridade (maior para menor):");
                            carrosFinalizados.stream()
                                    .sorted(Comparator.comparingInt(c -> c.prioridade))
                                    .forEach(c -> {
                                        long t = (c.tempoFim - c.tempoInicio) / 1000;
                                        System.out.println(c.nome + " (Prioridade " + c.prioridade + ") - Tempo: " + t + "s");
                                    });

                            System.exit(0);
                        }

                        break;
                    }
                }
                Thread.sleep(1000);
            }

        } catch (InterruptedException e) {
            System.out.println("O uso do " + nome + " foi interrompido.");
        }
    }
}
