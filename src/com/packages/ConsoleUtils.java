package com.packages;

import java.io.IOException;

public class ConsoleUtils {
    // ✅ Improved clearScreen() for cross-platform support
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J"); // ANSI escape code (Linux/macOS)
                System.out.flush();
            }
        } catch (IOException | InterruptedException ex) {
            for (int i = 0; i < 10; i++) System.out.println(); // Fallback: Print blank lines
        }
    }
    // Helper method to check if all characters in the search term appear in the name in order
    public static boolean containsCharactersInOrder(String name, String searchTerm) {
        int index = -1;
        for (char c : searchTerm.toCharArray()) {
            index = name.indexOf(c, index + 1);
            if (index == -1) {
                return false;
            }
        }
        return true;
    }
}
