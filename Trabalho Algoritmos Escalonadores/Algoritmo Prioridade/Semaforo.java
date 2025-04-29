class Semaforo extends Thread {
    private String nome;
    private String estado; // Cor na qual o semáforo se encontra

    private final Object lock = new Object();
    // Usamos o lock para que possamos usar múltiplas threads sem que elas interfiram no funcionamento uma da outra.

    public Semaforo(String nome) {
        this.nome = nome;
        this.estado = "vermelho"; // O semáforo começa fechado
    }

    public void run() {
        try {
            while (true) {
                synchronized (lock) {
                    estado = "verde";
                    System.out.println(nome + " está VERDE \uD83D\uDFE2");
                }

                Thread.sleep(5000); // O semáforo se mantém verde por 5 segundos

                synchronized (lock) {
                    estado = "amarelo";
                    System.out.println(nome + " está AMARELO \uD83D\uDFE1");
                }

                Thread.sleep(2000); // O semáforo se mantém amarelo por 2 segundos

                synchronized (lock) {
                    estado = "vermelho";
                    System.out.println(nome + " está VERMELHO \uD83D\uDD34");
                }

                Thread.sleep(5000); // // O semáforo se mantém vermelho por 5 segundos
            }
        } catch (InterruptedException e) {
            System.out.println("O uso do " + nome + "foi interrompido.");
        }
    }

    public boolean atravessar() { // Verifica se o semáforo está verde.
        synchronized (lock) {
            return estado.equals("verde");
        }
    }
}