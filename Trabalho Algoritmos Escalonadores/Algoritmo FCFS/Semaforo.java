class Semaforo extends Thread {
    private String nome;
    private String estado;
    private final Object lock = new Object();

    public Semaforo(String nome) {
        this.nome = nome;
        this.estado = "vermelho";
    }

    public void run() {
        try {
            while (true) {
                synchronized (lock) {
                    estado = "verde";
                    System.out.println(nome + " está VERDE 🟢");
                }

                Thread.sleep(5000);

                synchronized (lock) {
                    estado = "amarelo";
                    System.out.println(nome + " está AMARELO 🟡");
                }

                Thread.sleep(2000);

                synchronized (lock) {
                    estado = "vermelho";
                    System.out.println(nome + " está VERMELHO 🔴");
                }

                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            System.out.println("O uso do " + nome + " foi interrompido.");
        }
    }

    public boolean atravessar() {
        synchronized (lock) {
            return estado.equals("verde");
        }
    }
}