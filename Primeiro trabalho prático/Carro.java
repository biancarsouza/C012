class Carro extends Thread {
    private String nome;
    private Semaforo semaforo1;
    private Semaforo semaforo2;

    private static int qtdCarros = 0;

    public Carro(String nome, Semaforo semaforo1, Semaforo semaforo2) {
        this.nome = nome;
        this.semaforo1 = semaforo1;
        this.semaforo2 = semaforo2;
    }

    public void run() {
        try {
            System.out.println(nome + " entrou na FILA do PRIMEIRO semáforo.");

            while (true) {
                if (semaforo1.atravessar()) {
                    System.out.println(nome + " está ATRAVESSANDO o PRIMEIRO semáforo.");
                    Thread.sleep(2000);
                    System.out.println(nome + " PASSOU pelo PRIMEIRO semáforo!");
                    break;
                }
                Thread.sleep(1000);
            }

            Thread.sleep(2000);

            System.out.println(nome + " entrou na FILA do SEGUNDO semáforo.");

            while (true) {
                if (semaforo2.atravessar()) {
                    System.out.println(nome + " está ATRAVESSANDO o SEGUNDO semáforo.");
                    Thread.sleep(2000);
                    System.out.println(nome + " PASSOU pelo SEGUNDO semáforo e FINALIZOU o percurso!");

                    synchronized (Carro.class) {
                        qtdCarros++;
                        if (qtdCarros == 5) {
                            System.out.println("Todos os carros finalizaram o percurso.");
                            System.exit(0);
                        }
                    }
                    break;
                }
                Thread.sleep(1000);
            }

        } catch (InterruptedException e) {
            System.out.println("O uso do " + nome + " foi interrompido.");
        }
    }
}