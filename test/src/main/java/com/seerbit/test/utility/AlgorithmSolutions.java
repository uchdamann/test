package com.seerbit.test.utility;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AlgorithmSolutions {
    // Algorithm 1: Check for Numbers Summing up to a Target Algorithm
    public static boolean hasTwoSum(int[] nums, int target) {
        // Create a set to store the complement of each number
        Set<Integer> complementSet = new HashSet<>();

        // Iterate through the array
        for (int num : nums) {
            // Check if the complement of the current number exists in the set
            if (complementSet.contains(num)) {
                return true; // Found a pair whose sum is equal to the target
            }

            // Add the complement of the current number to the set
            int complement = target - num;
            complementSet.add(complement);
        }

        return false; // No pair found
    }

    // Algorithm 2: Low and High Index Searching Algorithm
    public static int[] searchIndexRange(int[] nums, int target) {
        int[] result = {-1, -1};
        int low = findLowIndex(nums, target);

        if (low == -1) {
            return result;
        }

        int high = findHighIndex(nums, target);

        result[0] = low;
        result[1] = high;

        return result;
    }

    private static int findLowIndex(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;

        while (low < high) {
            int mid = low + (high - low) / 2;

            if (nums[mid] < target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }

        if (nums[low] == target) {
            return low;
        }

        return -1;
    }

    private static int findHighIndex(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;

        while (low < high) {
            int mid = low + (high - low + 1) / 2;

            if (nums[mid] > target) {
                high = mid - 1;
            } else {
                low = mid;
            }
        }

        if (nums[high] == target) {
            return high;
        }

        return -1;
    }

    // Algorithm 3: Interval Merging Algorithm
    public static List<Interval> mergeIntervals(List<Interval> intervals) {
        // Create a list to store the merged intervals
        List<Interval> mergedIntervals = new ArrayList<>();

        // Iterate through the intervals
        for (Interval interval : intervals) {
            // If the mergedIntervals list is empty or the current interval does not overlap with the last interval in the list
            if (mergedIntervals.isEmpty() || interval.start > mergedIntervals.get(mergedIntervals.size() - 1).end) {
                mergedIntervals.add(interval); // Add the current interval to the mergedIntervals list
            } else {
                // Merge the current interval with the last interval in the mergedIntervals list
                mergedIntervals.get(mergedIntervals.size() - 1).end = Math.max(interval.end, mergedIntervals.get(mergedIntervals.size() - 1).end);
            }
        }

        return mergedIntervals;
    }


    // Interval class representing a pair of start and end timestamps
    static class Interval {
        int start;
        int end;

        Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    public static void main(String[] args) {
        int target;

        // Test for Algorithm 1
        int[] nums1 = {2, 4, 6, 9, 11};
        target = 14;

        boolean hasTwoSum = hasTwoSum(nums1, target);
        System.out.println("Algorithm 1:");
        System.out.println("Has Two Sum: " + hasTwoSum + "\n");


        // Test for Algorithm 2
        int[] nums2 = {2, 3, 4, 4, 4, 5, 5, 6};
        target = 5;

        int[] result = searchIndexRange(nums2, target);

        System.out.println("Algorithm 2:");
        System.out.println("Low Index: " + result[0]);
        System.out.println("High Index: " + result[1] + "\n");


        // Test for Algorithm 3
        // Example input array of interval pairs
        List<Interval> intervals = new ArrayList<>();
        intervals.add(new Interval(1, 3));
        intervals.add(new Interval(2, 6));
        intervals.add(new Interval(8, 10));
        intervals.add(new Interval(9, 13));
        intervals.add(new Interval(15, 18));

        // Merge the overlapping intervals
        List<Interval> mergedIntervals = mergeIntervals(intervals);

        // Print the merged intervals
        System.out.println("Algorithm 3:");
        for (Interval interval : mergedIntervals) {
            System.out.println(interval.start + " - " + interval.end);
        }
    }

}
