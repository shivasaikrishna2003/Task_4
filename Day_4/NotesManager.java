package Day_4;

import java.io.*;
import java.util.Scanner;

public class NotesManager {
    private static final String DEFAULT_FILE = "notes.txt";
    private String filePath;
    private final Scanner scanner;

    public NotesManager() {
        this.filePath = DEFAULT_FILE;
        this.scanner = new Scanner(System.in);
        ensureFileExists(filePath);
    }

    public static void main(String[] args) {
        NotesManager app = new NotesManager();
        app.run();
    }

    private void run() {
        System.out.println("\n=== Simple Notes Manager ===");
        System.out.println("Data file: " + new File(filePath).getAbsolutePath());

        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    addNote();
                    break;
                case "2":
                    viewNotes();
                    break;
                case "3":
                    clearNotes();
                    break;
                case "4":
                    changeFile();
                    break;
                case "5":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Try 1-5.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\nChoose an option:");
        System.out.println("1) Add a note (single or multi-line)");
        System.out.println("2) View all notes");
        System.out.println("3) Clear notes file");
        System.out.println("4) Change notes file");
        System.out.println("5) Exit");
        System.out.print("Enter choice: ");
    }

    /**
     * Writes note text to the file using FileWriter in append mode.
     * Demonstrates: new FileWriter(filePath, true) + write(...)
     */
    private void addNote() {
        System.out.println("\nType your note. Press ENTER on an empty line to finish:");
        StringBuilder sb = new StringBuilder();
        while (true) {
            String line = scanner.nextLine();
            if (line.isEmpty()) break; // stop on blank line
            sb.append(line).append(System.lineSeparator());
        }

        if (sb.length() == 0) {
            System.out.println("No text entered. Nothing saved.");
            return;
        }

        // Use FileWriter in append mode (true) to persist notes
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(sb.toString());
            // Optional separator for readability
            writer.write("---" + System.lineSeparator());
            System.out.println("Note saved to '" + filePath + "'.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Reads notes using FileReader + BufferedReader and prints to console.
     * Demonstrates: new FileReader(filePath) + BufferedReader.readLine()
     */
    private void viewNotes() {
        System.out.println("\n=== Notes in '" + filePath + "' ===");
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            System.out.println("(File is empty or does not exist.)");
            return;
        }

        try (FileReader fr = new FileReader(filePath);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            int lineNo = 1;
            while ((line = br.readLine()) != null) {
                System.out.printf("%3d | %s%n", lineNo++, line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Clears the file by creating a FileWriter with append=false (truncate).
     */
    private void clearNotes() {
        System.out.print("\nAre you sure you want to clear all notes? (y/N): ");
        String ans = scanner.nextLine().trim().toLowerCase();
        if (!ans.equals("y")) {
            System.out.println("Cancelled.");
            return;
        }
        try (FileWriter writer = new FileWriter(filePath, false)) { // overwrite
            // writing nothing truncates the file
            System.out.println("All notes cleared in '" + filePath + "'.");
        } catch (IOException e) {
            System.out.println("Error clearing file: " + e.getMessage());
        }
    }

    /**
     * Switch to a different file path and create it if needed.
     */
    private void changeFile() {
        System.out.print("\nEnter new file path (e.g., mynotes.txt or C:/temp/notes.txt): ");
        String newPath = scanner.nextLine().trim();
        if (newPath.isEmpty()) {
            System.out.println("No change.");
            return;
        }
        this.filePath = newPath;
        ensureFileExists(filePath);
        System.out.println("Now using: " + new File(filePath).getAbsolutePath());
    }

    /**
     * Utility: create the file if it doesn't exist.
     */
    private void ensureFileExists(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                File parent = f.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                if (f.createNewFile()) {
                    System.out.println("Created new notes file: " + f.getAbsolutePath());
                }
            }
        } catch (IOException e) {
            System.out.println("Warning: could not create file '" + path + "': " + e.getMessage());
        }
    }
}

