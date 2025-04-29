import java.util.*;

class Carro extends Thread {
    private String nome;
    private int prioridade; // Menor número = mais prioridade
    private Semaforo semaforo1;
    private Semaforo semaforo2;

    private static final Object lock = new Object();
    private static Queue<Carro> filaSemaforo = new LinkedList<>();

    private static int qtdCarros = 0;
    private static List<String> ordemFinalizacao = new ArrayList<>();

    public Carro(String nome, int prioridade, Semaforo semaforo1, Semaforo semaforo2) {
        this.nome = nome;
        this.prioridade = prioridade;
        this.semaforo1 = semaforo1;
        this.semaforo2 = semaforo2;
    }

    public void run() {
        try {
            synchronized (lock) {
                filaSemaforo.add(this);
                System.out.println(nome + " (Prioridade " + prioridade + ") entrou na FILA do PRIMEIRO semáforo.");
            }

            while (true) {
                synchronized (lock) {
                    if (filaSemaforo.peek() == this && semaforo1.atravessar()) {
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
                System.out.println(nome + " (Prioridade " + prioridade + ") entrou na FILA do SEGUNDO semáforo.");
            }

            while (true) {
                synchronized (lock) {
                    if (filaSemaforo.peek() == this && semaforo2.atravessar()) {
                        filaSemaforo.poll();
                        System.out.println(nome + " está ATRAVESSANDO o SEGUNDO semáforo.");

                        Thread.sleep(2000);

                        System.out.println(nome + " PASSOU pelo SEGUNDO semáforo e FINALIZOU o percurso!");

                        synchronized (lock) {
                            ordemFinalizacao.add(nome + " (Prioridade " + prioridade + ")");
                            qtdCarros++;

                            if (qtdCarros == 5) {
                                System.out.println("\n--- ORDEM DE FINALIZAÇÃO DOS CARROS ---");
                                for (int i = 0; i < ordemFinalizacao.size(); i++) {
                                    System.out.println((i + 1) + "º - " + ordemFinalizacao.get(i));
                                }
                                System.exit(0);
                            }
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