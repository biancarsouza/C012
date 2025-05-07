class Semaforo extends Thread {
    private String nome;
    private String estado; // Cor na qual o semáforo se encontra

    public Semaforo(String nome) {
        this.nome = nome;
        this.estado = "vermelho"; // O semáforo começa fechado
    }

    public void run() {
        try {
            while (true) {
                estado = "verde";
                System.out.println(nome + " está VERDE \uD83D\uDFE2");
                Thread.sleep(5000);

                estado = "amarelo";
                System.out.println(nome + " está AMARELO \uD83D\uDFE1");
                Thread.sleep(2000);

                estado = "vermelho";
                System.out.println(nome + " está VERMELHO \uD83D\uDD34");
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            System.out.println("O uso do " + nome + "foi interrompido.");
        }
    }

    public boolean atravessar() { // Verifica se o semáforo está verde.
        return estado.equals("verde");
    }
}