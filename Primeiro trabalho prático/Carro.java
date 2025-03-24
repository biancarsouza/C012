class Carro extends Thread {
    private String nome;
    private Semaforo semaforo1;
    private Semaforo semaforo2;

    private static final Object lock = new Object();
    // Sincroniza a passagem dos carros para que passe apenas um carro por vez.

    private static int qtdCarros = 0; // Conta quantos carros terminaram o trajeto

    public Carro(String nome, Semaforo semaforo1, Semaforo semaforo2) {
        this.nome = nome;
        this.semaforo1 = semaforo1;
        this.semaforo2 = semaforo2;
    }

    public void run() {
        try {
            System.out.println(nome + " está ESPERANDO no PRIMEIRO semáforo ⏳");

            while (true) {
                synchronized (lock) {
                    if (semaforo1.atravessar()) {
                        System.out.println(nome + " está ATRAVESSANDO o PRIMEIRO semáforo.");

                        Thread.sleep(2000); // Tempo que um carro leva para atravessar

                        System.out.println(nome + " PASSOU pelo PRIMEIRO semáforo!");

                        break;
                    }
                }

                Thread.sleep(1000); // Tempo para checar o estado do semáforo novamente
            }

            Thread.sleep(2000); // Tempo para continuar o trajeto

            System.out.println(nome + " está ESPERANDO no SEGUNDO semáforo ⏳");

            while (true) {
                synchronized (lock) {
                    if (semaforo2.atravessar()) {
                        System.out.println(nome + " está ATRAVESSANDO o SEGUNDO semáforo.");

                        Thread.sleep(2000); // Tempo que um carro leva para atravessar

                        System.out.println(nome + " PASSOU pelo SEGUNDO semáforo e FINALIZOU o percurso!");

                        qtdCarros++;

                        if (qtdCarros == 5) {
                            System.out.println("Todos os carros finalizaram o percurso.");
                            System.exit(0); // Finaliza o programa
                        }

                        Thread.sleep(5000); // Tempo antes do próximo carro

                        break;
                    }
                }

                Thread.sleep(1000); // // Tempo para checar o estado do semáforo novamente
            }

        } catch (InterruptedException e) {
            System.out.println("O uso do " + nome + "foi interrompido.");
        }
    }
}