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

    private static java.util.List<Carro> carrosFinalizados = new java.util.ArrayList<>();
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
        try {
            // Adiciona o carro à fila do primeiro semáforo de forma sincronizada
            synchronized (lock) {
                filaSemaforo.add(this);
                System.out.println(nome + " (Prioridade " + prioridade + ") entrou na FILA do PRIMEIRO semáforo.");
            }

            // Aguarda sua vez para atravessar o primeiro semáforo
            while (true) {
                synchronized (lock) {
                    Carro primeiro = filaSemaforo.peek();
                    if (primeiro == this && semaforo1.atravessar()) {
                        filaSemaforo.poll(); // Remove o carro da fila após atravessar
                        System.out.println(nome + " está ATRAVESSANDO o PRIMEIRO semáforo.");
                        Thread.sleep(2000); // Simula o tempo de travessia
                        System.out.println(nome + " PASSOU pelo PRIMEIRO semáforo!");
                        break;
                    }
                }
                Thread.sleep(1000); // Espera antes de tentar novamente
            }

            Thread.sleep(2000); // Simula tempo de deslocamento até o próximo semáforo

            // Adiciona o carro à fila do segundo semáforo de forma sincronizada
            synchronized (lock) {
                filaSemaforo.add(this);
                // Reordena a fila por prioridade (menor valor = maior prioridade)
                List<Carro> ordenada = new ArrayList<>(filaSemaforo);
                ordenada.sort(Comparator.comparingInt(c -> c.prioridade));
                filaSemaforo = new LinkedList<>(ordenada);

                System.out.println(nome + " (Prioridade " + prioridade + ") entrou na FILA do SEGUNDO semáforo.");
            }

            // Aguarda sua vez para atravessar o segundo semáforo
            while (true) {
                synchronized (lock) {
                    Carro primeiro = filaSemaforo.peek();
                    if (primeiro == this && semaforo2.atravessar()) {
                        filaSemaforo.poll(); // Remove o carro da fila após atravessar
                        System.out.println(nome + " está ATRAVESSANDO o SEGUNDO semáforo.");
                        Thread.sleep(2000); // Simula o tempo de travessia
                        System.out.println(nome + " PASSOU pelo SEGUNDO semáforo e FINALIZOU o percurso!");

                        carrosFinalizados.add(this);
                        qtdCarros++;

                        // Se todos os carros finalizarem, exibe estatísticas
                        if (qtdCarros == 5) {
                            System.out.println("\n=== TODOS OS CARROS FINALIZARAM ===");

                            System.out.println("\nOrdem de término:");
                            for (int i = 0; i < carrosFinalizados.size(); i++) {
                                Carro c = carrosFinalizados.get(i);
                                System.out.println((i + 1) + "º - " + c.nome + " (Prioridade " + c.prioridade + ")");
                            }

                            System.out.println("\nOrdem de prioridade (maior para menor):");
                            carrosFinalizados.stream()
                                    .sorted(Comparator.comparingInt(c -> c.prioridade))
                                    .forEach(c -> System.out.println(c.nome + " (Prioridade " + c.prioridade + ")"));

                            System.exit(0);
                        }

                        break;
                    }
                }
                Thread.sleep(1000); // Espera antes de tentar novamente
            }

        } catch (InterruptedException e) {
            System.out.println("O uso do " + nome + " foi interrompido.");
        }
    }

}
