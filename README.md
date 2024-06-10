# TalkController

The `TalkController` is a REST controller that handles HTTP requests related to talks in the conference.

## Endpoints

- `GET /api/talks`: Fetches a list of all talks.
- `POST /api/talks`: Adds a new talk. The details of the talk should be provided in the request body as a `TalkDto` object.
- `POST /api/talks/bulk`: Adds multiple talks. The details of the talks should be provided in the request body as a list of `TalkDto` objects.
- `DELETE /api/talks/{id}`: Deletes the talk with the specified ID.
- `DELETE /api/talks/bulk`: Deletes all talks.

## Request  Formats

### TalkDto

The `TalkDto` object has the following structure:

```json
{
    "title": "Talk Title",
    "duration": "60"
}
```
# TrackController
The `TrackController` is a REST controller that handles HTTP requests related to tracks in the conference.

## Endpoints

- `GET /api/tracks/schedule`: Fetches a formatted schedule of all tracks. Algorithms are used to schedule the talks in the tracks.
- `GET /api/tracks`: Fetches a list of all tracks.
- `DELETE /api/tracks/bulk`: Deletes all tracks and updates all talks to unscheduled.

# Subset Sum Finder for Talks - findSubsetSumForSession(List<Talk> talks, int targetSum) 

## Overview
Algorithm specifically designed to select a subset of talks whose total duration matches a given target sum. This can be particularly useful for scheduling conference sessions or any event planning where you need to fit a series of talks into a specific time slot.

## Algorithm Description

The `findSubsetSumForSession` function uses a recursive approach with memoization to efficiently find the subset of talks that match the target sum duration. This problem is a variation of the classic subset sum problem, which is NP-complete. The algorithm tries to include or exclude each talk recursively and uses a memoization technique to store and reuse previously computed results, thus improving performance.

### Key Components

- **Memoization:** A `Map<String, List<Talk>>` is used to store results of subproblems to avoid redundant calculations.
- **Recursive Approach:** The algorithm explores both possibilities for each talk - including it in the subset or excluding it, and proceeds recursively until it finds a solution or exhausts the possibilities.

## Code Explanation

### `findSubsetSumForSession`

```java
public List<Talk> findSubsetSumForSession(List<Talk> talks, int targetSum) {
    Map<String, List<Talk>> memo = new HashMap<>();
    return findSubsetSumHelper(talks, targetSum, talks.size() - 1, memo);
}
```
This is the core recursive function that performs the subset sum calculation:

1.Base Cases:
- If targetSum is 0, return an empty list (subset found).
- If targetSum is negative or index is out of bounds, return null (no valid subset).

2.Memoization Check:
- If the result for the current index and targetSum is already computed, return it from the memo map.

3.Recursive Calls:
- Exclude the current talk and check if a subset can be found with the remaining talks.
- Include the current talk and check if a subset can be found with the adjusted target sum and the remaining talks.

4.Result Storage:
- Store the result in the memo map and return it.

# Closest Subset Sum Finder for Talks  -  findClosestSubsetSumForSession(List<Talk> talks, int target)

## Overview
This repository contains an implementation of an algorithm to find a subset of talks whose total duration is the closest possible to a given target sum without exceeding it. This can be especially useful for scenarios like conference planning where the goal is to fill a session with talks that are as close as possible to a specific duration.

## Algorithm Description

The `findClosestSubsetSumForSession` function uses a dynamic programming approach to solve the closest subset sum problem. The algorithm first determines the closest possible sum to the target using a boolean array to track achievable sums and then reconstructs the subset that yields this sum.

### Key Components

- **Dynamic Programming Table:** A boolean array `dp` is used to track which sums can be achieved with subsets of the given talks.
- **Closest Sum Determination:** The algorithm finds the highest achievable sum that does not exceed the target.
- **Subset Reconstruction:** The algorithm traces back through the dynamic programming table to determine which talks contribute to the closest sum.

This function finds the closest subset sum to the target and reconstructs the subset of talks that achieves this sum.

Steps

1.Initialization:

- A boolean array dp of size target + 1 is initialized to track which sums can be achieved with the given talks.
- dp[0] is set to true because a sum of 0 can always be achieved with an empty subset.

2.Dynamic Programming Table Population:

- For each talk, the dp table is updated in reverse order to avoid overwriting results from the same iteration. This ensures that for each possible duration, the dp table correctly reflects the achievable sums.

3.Closest Sum Determination:

- The algorithm searches the dp table from target down to 0 to find the highest achievable sum that does not exceed the target. This is the closestSum.

4.Subset Reconstruction:

- Starting from the closestSum, the algorithm traces back through the dp table to identify the talks that contribute to this sum.
- If including a talk reduces the current sum and the resulting sum is achievable (as indicated by the dp table), the talk is included in the subset.