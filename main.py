import random
import time
import sys

class SortingAlgorithmComparator:
    def __init__(self):
        self.data = []
        self.bubble_sort_steps = 0
        self.merge_sort_steps = 0
        self.quick_sort_steps = 0
        
    def display_menu(self):
        """Display the main menu"""
        print("\n" + "="*60)
        print("        Data Sorter: Sorting Algorithm Comparison Tool")
        print("="*60)
        print("1. Enter numbers manually")
        print("2. Generate random numbers")
        print("3. Perform Bubble Sort")
        print("4. Perform Merge Sort")
        print("5. Perform Quick Sort")
        print("6. Compare all algorithms (show performance table)")
        print("7. Exit")
        print("-"*60)
        
    def get_user_choice(self):
        """Get and validate user input"""
        try:
            choice = input("Enter your choice (1-7): ").strip()
            if not choice:
                print("Please enter a choice!")
                return -1
            choice = int(choice)
            if 1 <= choice <= 7:
                return choice
            else:
                print("Please enter a number between 1 and 7!")
                return -1
        except ValueError:
            print("Please enter a valid number!")
            return -1
    
    def input_manual_data(self):
        """Allow user to input custom numbers with robust error handling"""
        print("\n--- Manual Data Entry ---")
        try:
            data_str = input("Enter numbers separated by spaces: ").strip()
            if not data_str:
                print("No input received!")
                return
                
            # Split and convert to numbers
            numbers = []
            for item in data_str.split():
                try:
                    # Try to convert to float first, then int if possible
                    num = float(item)
                    if num.is_integer():
                        num = int(num)
                    numbers.append(num)
                except ValueError:
                    print(f"Warning: '{item}' is not a valid number and will be skipped.")
            
            if not numbers:
                print("No valid numbers entered!")
                return
                
            self.data = numbers
            print(f"Successfully entered {len(self.data)} numbers")
            print(f"Data: {self.data}")
            
        except Exception as e:
            print(f"An unexpected error occurred: {e}")
    
    def generate_random_data(self):
        """Generate random dataset with comprehensive error handling"""
        print("\n--- Random Data Generation ---")
        try:
            size_input = input("Enter the number of elements to generate: ").strip()
            if not size_input:
                print("No input received!")
                return
                
            size = int(size_input)
            if size <= 0:
                print("Size must be a positive integer!")
                return
            if size > 10000:
                print("Warning: Large dataset size may cause performance issues!")
                confirm = input("Continue? (y/n): ").strip().lower()
                if confirm != 'y':
                    return
            
            min_val_input = input("Enter minimum value: ").strip()
            max_val_input = input("Enter maximum value: ").strip()
            
            if not min_val_input or not max_val_input:
                print("Please enter both minimum and maximum values!")
                return
                
            min_val = int(min_val_input)
            max_val = int(max_val_input)
            
            if min_val > max_val:
                print("Minimum value cannot be greater than maximum value!")
                min_val, max_val = max_val, min_val
                print(f"Swapped values: min={min_val}, max={max_val}")
            
            self.data = [random.randint(min_val, max_val) for _ in range(size)]
            print(f"Generated {size} random numbers between {min_val} and {max_val}")
            
            # Show sample without causing index errors
            sample_size = min(10, len(self.data))
            sample = self.data[:sample_size]
            if len(self.data) > 10:
                print(f"Sample: {sample}...")
            else:
                print(f"Data: {sample}")
            
        except ValueError as ve:
            print(f"Invalid input! Please enter integers only. Error: {ve}")
        except Exception as e:
            print(f"An unexpected error occurred: {e}")
    
    def bubble_sort(self, arr):
        """Bubble Sort implementation with step counting"""
        if not arr:
            return [], 0
            
        try:
            n = len(arr)
            steps = 0
            arr_copy = arr.copy()
            
            for i in range(n):
                swapped = False
                for j in range(0, n - i - 1):
                    steps += 1  # Comparison step
                    if arr_copy[j] > arr_copy[j + 1]:
                        # Swap elements
                        arr_copy[j], arr_copy[j + 1] = arr_copy[j + 1], arr_copy[j]
                        steps += 3  # Swap operation (3 steps: two reads + one write)
                        swapped = True
                
                steps += 1  # Pass completion check
                if not swapped:
                    break
                    
            return arr_copy, steps
        except Exception as e:
            print(f"Error in Bubble Sort: {e}")
            return arr.copy(), 0
    
    def merge_sort(self, arr):
        """Merge Sort implementation with step counting"""
        if not arr:
            return [], 0
            
        try:
            steps = [0]  # Use list to track steps across recursive calls
            
            def merge_sort_recursive(sub_arr):
                if len(sub_arr) <= 1:
                    return sub_arr
                    
                mid = len(sub_arr) // 2
                steps[0] += 2  # Division and slice operations
                
                left = merge_sort_recursive(sub_arr[:mid])
                right = merge_sort_recursive(sub_arr[mid:])
                
                return merge(left, right)
            
            def merge(left, right):
                merged = []
                i = j = 0
                
                while i < len(left) and j < len(right):
                    steps[0] += 1  # Comparison step
                    if left[i] <= right[j]:
                        merged.append(left[i])
                        i += 1
                    else:
                        merged.append(right[j])
                        j += 1
                    steps[0] += 2  # Append and increment steps
                
                # Add remaining elements
                while i < len(left):
                    merged.append(left[i])
                    i += 1
                    steps[0] += 2
                
                while j < len(right):
                    merged.append(right[j])
                    j += 1
                    steps[0] += 2
                    
                return merged
            
            arr_copy = arr.copy()
            sorted_arr = merge_sort_recursive(arr_copy)
            return sorted_arr, steps[0]
        except Exception as e:
            print(f"Error in Merge Sort: {e}")
            return arr.copy(), 0
    
    def quick_sort(self, arr):
        """Quick Sort implementation with step counting"""
        if not arr:
            return [], 0
            
        try:
            steps = [0]
            
            def quick_sort_recursive(sub_arr):
                if len(sub_arr) <= 1:
                    return sub_arr
                
                # Choose middle element as pivot
                pivot = sub_arr[len(sub_arr) // 2]
                steps[0] += 2  # Division and indexing
                
                left = []
                right = []
                middle = []
                
                # Partition the array
                for x in sub_arr:
                    steps[0] += 1  # Comparison step
                    if x < pivot:
                        left.append(x)
                    elif x > pivot:
                        right.append(x)
                    else:
                        middle.append(x)
                    steps[0] += 1  # Append step
                
                steps[0] += 1  # Final step for partition completion
                
                return quick_sort_recursive(left) + middle + quick_sort_recursive(right)
            
            arr_copy = arr.copy()
            sorted_arr = quick_sort_recursive(arr_copy)
            return sorted_arr, steps[0]
        except Exception as e:
            print(f"Error in Quick Sort: {e}")
            return arr.copy(), 0
    
    def perform_bubble_sort(self):
        """Perform Bubble Sort and display results"""
        if not self.data:
            print("No data available! Please enter or generate data first.")
            return
            
        print("\n--- Bubble Sort ---")
        print(f"Original data ({len(self.data)} elements): {self.data}")
        
        try:
            start_time = time.perf_counter()
            sorted_data, steps = self.bubble_sort(self.data)
            end_time = time.perf_counter()
            
            self.bubble_sort_steps = steps
            execution_time = (end_time - start_time) * 1000  # Convert to milliseconds
            
            print(f"Sorted data: {sorted_data}")
            print(f"Execution time: {execution_time:.6f} milliseconds")
            print(f"Number of steps: {steps}")
            print(f"Time per step: {execution_time/steps:.6f} ms/step" if steps > 0 else "N/A")
            
        except Exception as e:
            print(f"Error during Bubble Sort execution: {e}")
    
    def perform_merge_sort(self):
        """Perform Merge Sort and display results"""
        if not self.data:
            print("No data available! Please enter or generate data first.")
            return
            
        print("\n--- Merge Sort ---")
        print(f"Original data ({len(self.data)} elements): {self.data}")
        
        try:
            start_time = time.perf_counter()
            sorted_data, steps = self.merge_sort(self.data)
            end_time = time.perf_counter()
            
            self.merge_sort_steps = steps
            execution_time = (end_time - start_time) * 1000  # Convert to milliseconds
            
            print(f"Sorted data: {sorted_data}")
            print(f"Execution time: {execution_time:.6f} milliseconds")
            print(f"Number of steps: {steps}")
            print(f"Time per step: {execution_time/steps:.6f} ms/step" if steps > 0 else "N/A")
            
        except Exception as e:
            print(f"Error during Merge Sort execution: {e}")
    
    def perform_quick_sort(self):
        """Perform Quick Sort and display results"""
        if not self.data:
            print("No data available! Please enter or generate data first.")
            return
            
        print("\n--- Quick Sort ---")
        print(f"Original data ({len(self.data)} elements): {self.data}")
        
        try:
            start_time = time.perf_counter()
            sorted_data, steps = self.quick_sort(self.data)
            end_time = time.perf_counter()
            
            self.quick_sort_steps = steps
            execution_time = (end_time - start_time) * 1000  # Convert to milliseconds
            
            print(f"Sorted data: {sorted_data}")
            print(f"Execution time: {execution_time:.6f} milliseconds")
            print(f"Number of steps: {steps}")
            print(f"Time per step: {execution_time/steps:.6f} ms/step" if steps > 0 else "N/A")
            
        except Exception as e:
            print(f"Error during Quick Sort execution: {e}")
    
    def compare_all_algorithms(self):
        """Compare all sorting algorithms and display performance table"""
        if not self.data:
            print("No data available! Please enter or generate data first.")
            return
        
        print("\n" + "="*80)
        print("               PERFORMANCE COMPARISON TABLE")
        print("="*80)
        print(f"Dataset size: {len(self.data)} elements")
        
        # Show sample without causing index errors
        sample_size = min(8, len(self.data))
        sample = self.data[:sample_size]
        if len(self.data) > 8:
            print(f"Dataset sample: {sample}...")
        else:
            print(f"Dataset: {sample}")
        print("-"*80)
        print(f"{'Algorithm':<15} {'Time (ms)':<15} {'Steps':<15} {'Time/Step (ms)':<15} {'Efficiency':<20}")
        print("-"*80)
        
        try:
            # Test Bubble Sort
            start_time = time.perf_counter()
            bubble_sorted, bubble_steps = self.bubble_sort(self.data)
            bubble_time = (time.perf_counter() - start_time) * 1000
            
            # Test Merge Sort
            start_time = time.perf_counter()
            merge_sorted, merge_steps = self.merge_sort(self.data)
            merge_time = (time.perf_counter() - start_time) * 1000
            
            # Test Quick Sort
            start_time = time.perf_counter()
            quick_sorted, quick_steps = self.quick_sort(self.data)
            quick_time = (time.perf_counter() - start_time) * 1000
            
            # Calculate time per step (avoid division by zero)
            bubble_time_per_step = bubble_time / bubble_steps if bubble_steps > 0 else 0
            merge_time_per_step = merge_time / merge_steps if merge_steps > 0 else 0
            quick_time_per_step = quick_time / quick_steps if quick_steps > 0 else 0
            
            # Determine efficiency labels
            bubble_eff = "O(n²) - Slow"
            merge_eff = "O(n log n) - Fast"
            quick_eff = "O(n log n) - Fastest*"
            
            print(f"{'Bubble Sort':<15} {bubble_time:<15.6f} {bubble_steps:<15} {bubble_time_per_step:<15.6f} {bubble_eff:<20}")
            print(f"{'Merge Sort':<15} {merge_time:<15.6f} {merge_steps:<15} {merge_time_per_step:<15.6f} {merge_eff:<20}")
            print(f"{'Quick Sort':<15} {quick_time:<15.6f} {quick_steps:<15} {quick_time_per_step:<15.6f} {quick_eff:<20}")
            print("-"*80)
            
            # Determine the fastest algorithm
            times = [bubble_time, merge_time, quick_time]
            algorithms = ["Bubble Sort", "Merge Sort", "Quick Sort"]
            fastest_index = times.index(min(times))
            
            print(f"Fastest algorithm: {algorithms[fastest_index]}")
            print("* Quick Sort performance may vary based on pivot selection and data distribution")
            print("="*80)
            
            # Store the step counts for individual display
            self.bubble_sort_steps = bubble_steps
            self.merge_sort_steps = merge_steps
            self.quick_sort_steps = quick_steps
            
        except Exception as e:
            print(f"Error during performance comparison: {e}")
    
    def clear_data(self):
        """Clear current data"""
        self.data = []
        print("Data cleared!")
    
    def show_current_data(self):
        """Show current data"""
        if self.data:
            print(f"Current data ({len(self.data)} elements): {self.data}")
        else:
            print("No data available!")
    
    def run(self):
        """Main program loop with comprehensive error handling"""
        print("Welcome to the Sorting Algorithm Comparison Tool!")
        print("This tool compares Bubble Sort, Merge Sort, and Quick Sort algorithms.")
        
        while True:
            try:
                self.display_menu()
                choice = self.get_user_choice()
                
                if choice == 1:
                    self.input_manual_data()
                elif choice == 2:
                    self.generate_random_data()
                elif choice == 3:
                    self.perform_bubble_sort()
                elif choice == 4:
                    self.perform_merge_sort()
                elif choice == 5:
                    self.perform_quick_sort()
                elif choice == 6:
                    self.compare_all_algorithms()
                elif choice == 7:
                    print("\nThank you for using the Sorting Algorithm Comparison Tool!")
                    print("Goodbye!")
                    break
                elif choice == -1:
                    # Invalid choice, already handled in get_user_choice
                    continue
                else:
                    print("Invalid choice! Please enter a number between 1-7.")
                    
            except KeyboardInterrupt:
                print("\n\nProgram interrupted by user. Exiting gracefully...")
                break
            except Exception as e:
                print(f"\nAn unexpected error occurred: {e}")
                print("The program will continue running...")
                
            # Small pause for better user experience
            if choice != 7:
                input("\nPress Enter to continue...")

# Main execution with error handling
if __name__ == "__main__":
    try:
        app = SortingAlgorithmComparator()
        app.run()
    except KeyboardInterrupt:
        print("\n\nProgram terminated by user.")
    except Exception as e:
        print(f"\nA critical error occurred: {e}")
        print("The program will now exit.")
    finally:
        print("Thank you for using the Sorting Algorithm Comparison Tool!")