import java.util.LinkedList;
import java.util.Queue;

class Carro extends Thread {
    private String nome;
    private Semaforo semaforo1;
    private Semaforo semaforo2;

    // Sincroniza a passagem dos carros para que passe apenas um carro por vez.
    private static final Object lock = new Object();

    // Fila para controlar a ordem de travessia dos semáforos
    private static Queue<Carro> filaSemaforo = new LinkedList<>();

    // Contador de carros que finalizaram o percurso
    private static int qtdCarros = 0;

    public Carro(String nome, Semaforo semaforo1, Semaforo semaforo2) {
        this.nome = nome;
        this.semaforo1 = semaforo1;
        this.semaforo2 = semaforo2;
    }

    public void run() {
        try {
            // Adiciona o carro à fila do primeiro semáforo de forma sincronizada
            synchronized (lock) {
                filaSemaforo.add(this);
                System.out.println(nome + " entrou na FILA do PRIMEIRO semáforo.");
            }

            // Aguarda sua vez para atravessar o primeiro semáforo
            while (true) {
                synchronized (lock) {
                    if (filaSemaforo.peek() == this && semaforo1.atravessar()) {
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
                System.out.println(nome + " entrou na FILA do SEGUNDO semáforo.");
            }

            // Aguarda sua vez para atravessar o segundo semáforo
            while (true) {
                synchronized (lock) {
                    if (filaSemaforo.peek() == this && semaforo2.atravessar()) {
                        filaSemaforo.poll(); // Remove o carro da fila após atravessar
                        System.out.println(nome + " está ATRAVESSANDO o SEGUNDO semáforo.");

                        Thread.sleep(2000); // Simula o tempo de travessia

                        System.out.println(nome + " PASSOU pelo SEGUNDO semáforo e FINALIZOU o percurso!");

                        qtdCarros++; // Incrementa o número de carros que finalizaram

                        // Se 5 carros concluírem o percurso, o programa encerra
                        if (qtdCarros == 5) {
                            System.out.println("Todos os carros finalizaram o percurso.");
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
