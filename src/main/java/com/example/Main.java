package com.example;

public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java -cp <classpath> com.example.Main <server|client>");
            System.exit(1);
        }

        String mode = args[0];

        switch (mode) {
            case "server":
                PlayerServer.main(new String[0]);
                break;
            case "client":
                PlayerClient.main(new String[0]);
                break;
            case "normal":
                PlayerMain.main(new String[0]);
                break;

            default:
                System.err.println("Unknown mode: " + mode);
                System.exit(1);
        }
    }
}
