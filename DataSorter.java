import java.util.*;
import java.util.concurrent.TimeUnit;

public class DataSorter {
    private static class SortResult {
        int[] sortedArray;
        long executionTime;
        int steps;
        
        SortResult(int[] sortedArray, long executionTime, int steps) {
            this.sortedArray = sortedArray;
            this.executionTime = executionTime;
            this.steps = steps;
        }
    }
    
    private int[] data;
    private Scanner scanner;
    
    public DataSorter() {
        this.data = new int[0];
        this.scanner = new Scanner(System.in);
    }
    
    public void displayMenu() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("           Data Sorter: Sorting Algorithm Comparison Tool");
        System.out.println("=".repeat(70));
        System.out.println("1. Enter numbers manually");
        System.out.println("2. Generate random numbers");
        System.out.println("3. Perform Bubble Sort");
        System.out.println("4. Perform Merge Sort");
        System.out.println("5. Perform Quick Sort");
        System.out.println("6. Compare all algorithms (show performance table)");
        System.out.println("7. Exit");
        System.out.println("-".repeat(70));
    }
    
    public int getUserChoice() {
        try {
            System.out.print("Enter your choice (1-7): ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Please enter a choice!");
                return -1;
            }
            int choice = Integer.parseInt(input);
            if (choice >= 1 && choice <= 7) {
                return choice;
            } else {
                System.out.println("Please enter a number between 1 and 7!");
                return -1;
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");
            return -1;
        }
    }
    
    public void enterNumbersManually() {
        System.out.println("\n--- Manual Data Entry ---");
        try {
            System.out.print("Enter numbers separated by spaces: ");
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                System.out.println("No input received!");
                return;
            }
            
            String[] numberStrings = input.split("\\s+");
            int[] numbers = new int[numberStrings.length];
            int validCount = 0;
            
            for (int i = 0; i < numberStrings.length; i++) {
                try {
                    numbers[validCount] = Integer.parseInt(numberStrings[i]);
                    validCount++;
                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid number: " + numberStrings[i]);
                }
            }
            
            if (validCount == 0) {
                System.out.println("No valid numbers entered!");
                return;
            }
            
            
            this.data = Arrays.copyOf(numbers, validCount);
            System.out.println("Successfully entered " + validCount + " numbers");
            System.out.print("Current data: ");
            displayArray(this.data);
            
        } catch (Exception e) {
            System.out.println("An error occurred during data entry: " + e.getMessage());
        }
    }
    
    public void generateRandomNumbers() {
        System.out.println("\n--- Random Data Generation ---");
        try {
            System.out.print("Enter the number of elements to generate: ");
            int size = Integer.parseInt(scanner.nextLine().trim());
            
            if (size <= 0) {
                System.out.println("Size must be a positive integer!");
                return;
            }
            
            if (size > 10000) {
                System.out.println("Warning: Large datasets may take longer to sort!");
            }
            
            System.out.print("Enter minimum value: ");
            int min = Integer.parseInt(scanner.nextLine().trim());
            
            System.out.print("Enter maximum value: ");
            int max = Integer.parseInt(scanner.nextLine().trim());
            
            if (min > max) {
                System.out.println("Minimum cannot be greater than maximum. Swapping values.");
                int temp = min;
                min = max;
                max = temp;
            }
            
            Random random = new Random();
            this.data = new int[size];
            for (int i = 0; i < size; i++) {
                this.data[i] = random.nextInt(max - min + 1) + min;
            }
            
            System.out.println("Generated " + size + " random numbers between " + min + " and " + max);
            System.out.print("Sample data (first 10 elements): ");
            displayArraySample(this.data, 10);
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter valid integers!");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
    
    
    public SortResult bubbleSort(int[] array) {
        if (array == null || array.length == 0) {
            return new SortResult(new int[0], 0, 0);
        }
        
        int[] arr = array.clone();
        int steps = 0;
        long startTime = System.nanoTime();
        
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                steps++; // Comparison step
                if (arr[j] > arr[j + 1]) {
                    // Swap elements
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    steps += 3; 
                    swapped = true;
                }
            }
            steps++; 
            if (!swapped) break;
        }
        
        long endTime = System.nanoTime();
        long executionTime = endTime - startTime;
        
        return new SortResult(arr, executionTime, steps);
    }
    
    
    public SortResult mergeSort(int[] array) {
        if (array == null || array.length == 0) {
            return new SortResult(new int[0], 0, 0);
        }
        
        int[] arr = array.clone();
        int[] steps = {0}; 
        
        long startTime = System.nanoTime();
        int[] result = mergeSortRecursive(arr, steps);
        long endTime = System.nanoTime();
        long executionTime = endTime - startTime;
        
        return new SortResult(result, executionTime, steps[0]);
    }
    
    private int[] mergeSortRecursive(int[] array, int[] steps) {
        if (array.length <= 1) {
            return array;
        }
        
        int mid = array.length / 2;
        steps[0] += 2; 
        
        int[] left = Arrays.copyOfRange(array, 0, mid);
        int[] right = Arrays.copyOfRange(array, mid, array.length);
        
        left = mergeSortRecursive(left, steps);
        right = mergeSortRecursive(right, steps);
        
        return merge(left, right, steps);
    }
    
    private int[] merge(int[] left, int[] right, int[] steps) {
        int[] result = new int[left.length + right.length];
        int i = 0, j = 0, k = 0;
        
        while (i < left.length && j < right.length) {
            steps[0]++; 
            if (left[i] <= right[j]) {
                result[k++] = left[i++];
            } else {
                result[k++] = right[j++];
            }
            steps[0]++; 
        }
        
        while (i < left.length) {
            result[k++] = left[i++];
            steps[0]++; 
        }
        
        while (j < right.length) {
            result[k++] = right[j++];
            steps[0]++; 
        }
        
        return result;
    }
    
    
    public SortResult quickSort(int[] array) {
        if (array == null || array.length == 0) {
            return new SortResult(new int[0], 0, 0);
        }
        
        int[] arr = array.clone();
        int[] steps = {0}; 
        
        long startTime = System.nanoTime();
        quickSortRecursive(arr, 0, arr.length - 1, steps);
        long endTime = System.nanoTime();
        long executionTime = endTime - startTime;
        
        return new SortResult(arr, executionTime, steps[0]);
    }
    
    private void quickSortRecursive(int[] arr, int low, int high, int[] steps) {
        if (low < high) {
            int pi = partition(arr, low, high, steps);
            quickSortRecursive(arr, low, pi - 1, steps);
            quickSortRecursive(arr, pi + 1, high, steps);
        }
    }
    
    private int partition(int[] arr, int low, int high, int[] steps) {
        int pivot = arr[high];
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            steps[0]++; 
            if (arr[j] <= pivot) {
                i++;
                
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                steps[0] += 3; 
            }
        }
        
        
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        steps[0] += 3; 
        
        return i + 1;
    }
    
    
    public void performBubbleSort() {
        if (!checkDataExists()) return;
        
        System.out.println("\n--- Bubble Sort ---");
        System.out.print("Original data (" + data.length + " elements): ");
        displayArraySample(data, 15);
        
        SortResult result = bubbleSort(data);
        
        System.out.print("Sorted data: ");
        displayArraySample(result.sortedArray, 15);
        displaySortResults("Bubble Sort", result);
    }
    
    public void performMergeSort() {
        if (!checkDataExists()) return;
        
        System.out.println("\n--- Merge Sort ---");
        System.out.print("Original data (" + data.length + " elements): ");
        displayArraySample(data, 15);
        
        SortResult result = mergeSort(data);
        
        System.out.print("Sorted data: ");
        displayArraySample(result.sortedArray, 15);
        displaySortResults("Merge Sort", result);
    }
    
    public void performQuickSort() {
        if (!checkDataExists()) return;
        
        System.out.println("\n--- Quick Sort ---");
        System.out.print("Original data (" + data.length + " elements): ");
        displayArraySample(data, 15);
        
        SortResult result = quickSort(data);
        
        System.out.print("Sorted data: ");
        displayArraySample(result.sortedArray, 15);
        displaySortResults("Quick Sort", result);
    }
    
    public void compareAllAlgorithms() {
        if (!checkDataExists()) return;
        
        System.out.println("\n" + "=".repeat(90));
        System.out.println("                      PERFORMANCE COMPARISON TABLE");
        System.out.println("=".repeat(90));
        System.out.println("Dataset Information:");
        System.out.println("  Size: " + data.length + " elements");
        System.out.print("  Sample: ");
        displayArraySample(data, 8);
        System.out.println("-".repeat(90));
        
        
        SortResult bubbleResult = bubbleSort(data);
        SortResult mergeResult = mergeSort(data);
        SortResult quickResult = quickSort(data);
        
        
        System.out.printf("%-15s %-20s %-15s %-20s %-15s\n", 
                         "Algorithm", "Time (nanoseconds)", "Time (ms)", "Steps", "Efficiency");
        System.out.println("-".repeat(90));
        
        System.out.printf("%-15s %-20d %-15.3f %-20d %-15s\n", 
                         "Bubble Sort", 
                         bubbleResult.executionTime,
                         bubbleResult.executionTime / 1_000_000.0,
                         bubbleResult.steps,
                         "O(n²)");
        
        System.out.printf("%-15s %-20d %-15.3f %-20d %-15s\n", 
                         "Merge Sort", 
                         mergeResult.executionTime,
                         mergeResult.executionTime / 1_000_000.0,
                         mergeResult.steps,
                         "O(n log n)");
        
        System.out.printf("%-15s %-20d %-15.3f %-20d %-15s\n", 
                         "Quick Sort", 
                         quickResult.executionTime,
                         quickResult.executionTime / 1_000_000.0,
                         quickResult.steps,
                         "O(n log n)");
        
        System.out.println("-".repeat(90));
        
        
        String fastest = "Bubble Sort";
        long minTime = bubbleResult.executionTime;
        
        if (mergeResult.executionTime < minTime) {
            minTime = mergeResult.executionTime;
            fastest = "Merge Sort";
        }
        
        if (quickResult.executionTime < minTime) {
            fastest = "Quick Sort";
        }
        
        System.out.println("Fastest Algorithm: " + fastest);
        System.out.println("=".repeat(90));
        
        
        System.out.println("\nAlgorithm Complexity Analysis:");
        System.out.println("• Bubble Sort: O(n²) - Good for small datasets, inefficient for large data");
        System.out.println("• Merge Sort: O(n log n) - Consistent performance, uses extra memory");
        System.out.println("• Quick Sort: O(n log n) - Fastest in practice, performance depends on pivot");
    }
    
    private void displaySortResults(String algorithmName, SortResult result) {
        System.out.println("\n" + algorithmName + " Results:");
        System.out.println("  Execution Time: " + result.executionTime + " nanoseconds (" + 
                         String.format("%.3f", result.executionTime / 1_000_000.0) + " ms)");
        System.out.println("  Number of Steps: " + result.steps);
        System.out.println("  Time per Step: " + 
                         String.format("%.3f", (double)result.executionTime / result.steps) + " ns/step");
        
        
        if (isSorted(result.sortedArray)) {
            System.out.println("  ✓ Sorting verified: Array is correctly sorted");
        } else {
            System.out.println("  ✗ Sorting error: Array is not correctly sorted");
        }
    }
    
    private boolean checkDataExists() {
        if (data == null || data.length == 0) {
            System.out.println("No data available! Please enter or generate data first (Options 1 or 2).");
            return false;
        }
        return true;
    }
    
    private void displayArray(int[] array) {
        if (array.length <= 20) {
            System.out.println(Arrays.toString(array));
        } else {
            displayArraySample(array, 10);
        }
    }
    
    private void displayArraySample(int[] array, int sampleSize) {
        if (array.length <= sampleSize) {
            System.out.println(Arrays.toString(array));
        } else {
            System.out.print("[");
            for (int i = 0; i < sampleSize; i++) {
                System.out.print(array[i]);
                if (i < sampleSize - 1) System.out.print(", ");
            }
            System.out.println(", ...] (" + array.length + " elements total)");
        }
    }
    
    private boolean isSorted(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i + 1]) {
                return false;
            }
        }
        return true;
    }
    
    public void run() {
        System.out.println("Welcome to Data Sorter - Sorting Algorithm Comparison Tool!");
        System.out.println("This program compares Bubble Sort, Merge Sort, and Quick Sort algorithms.");
        
        while (true) {
            try {
                displayMenu();
                int choice = getUserChoice();
                
                switch (choice) {
                    case 1:
                        enterNumbersManually();
                        break;
                    case 2:
                        generateRandomNumbers();
                        break;
                    case 3:
                        performBubbleSort();
                        break;
                    case 4:
                        performMergeSort();
                        break;
                    case 5:
                        performQuickSort();
                        break;
                    case 6:
                        compareAllAlgorithms();
                        break;
                    case 7:
                        System.out.println("\nThank you for using Data Sorter!");
                        System.out.println("Goodbye!");
                        scanner.close();
                        return;
                    case -1:
                        continue;
                    default:
                        System.out.println("Invalid choice! Please enter a number between 1-7.");
                }
                
                if (choice != 7) {
                    System.out.print("\nPress Enter to continue...");
                    scanner.nextLine();
                }
                
            } catch (Exception e) {
                System.out.println("\nAn unexpected error occurred: " + e.getMessage());
                System.out.println("The program will continue running...");
                scanner.nextLine(); 
            }
        }
    }
    
    public static void main(String[] args) {
        try {
            DataSorter sorter = new DataSorter();
            sorter.run();
        } catch (Exception e) {
            System.out.println("A critical error occurred: " + e.getMessage());
            System.out.println("Program will now exit.");
        }
    }

    public int[] getData() {
        return data;
    }

    public void setData(int[] data) {
        this.data = data;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }
}