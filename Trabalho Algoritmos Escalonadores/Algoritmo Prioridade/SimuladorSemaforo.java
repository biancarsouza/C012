public class SimuladorSemaforo {
    public static void main(String[] args) {
        Semaforo semaforo1 = new Semaforo("Semáforo 1");
        Semaforo semaforo2 = new Semaforo("Semáforo 2");

        semaforo1.start();
        semaforo2.start();

        // Menor número = maior prioridade (ex: 1 = ambulância)
        Carro carro1 = new Carro("Ambulância", 1, semaforo1, semaforo2);
        Carro carro2 = new Carro("Carro 2", 3, semaforo1, semaforo2);
        Carro carro3 = new Carro("Carro 3", 5, semaforo1, semaforo2);
        Carro carro4 = new Carro("Polícia", 2, semaforo1, semaforo2);
        Carro carro5 = new Carro("Carro 5", 4, semaforo1, semaforo2);

        carro1.start();
        carro2.start();
        carro3.start();
        carro4.start();
        carro5.start();
    }
}
