# Water Tank Problem — Solution

## The problem in simple words

You are given an array of numbers. Each number is the **height of a block**
(like a wall). When it rains, water gets trapped in the **dips between taller
blocks**. We must count **how many units of water** stay trapped.

```
Input:  [0, 4, 0, 0, 0, 6, 0, 6, 4, 0]
Output: 18 units
```

## How water gets trapped (the key idea)

Water can only sit on top of a block if there is a **taller wall on its left
AND a taller wall on its right**. The water level at any position is limited by
the **shorter of those two walls** (water spills over the lower side).

> Water trapped above block `i` = `min(tallestLeft, tallestRight) − height[i]`
> (only if this is a positive number).

### Walking through the example

```
height:        0  4  0  0  0  6  0  6  4  0
tallest left:  0  4  4  4  4  6  6  6  6  6
tallest right: 6  6  6  6  6  6  6  6  4  0
min(L,R):      0  4  4  4  4  6  6  6  4  0
water = min - height:
               0  0  4  4  4  0  6  0  0  0   ->  4+4+4+6 = 18 ✅
```

Picture of the trapped water (`~` = water, `█` = block):

```
6              █  ~  █
5              █  ~  █
4     █  ~  ~  █  ~  █  █
3     █  ~  ~  █  ~  █  █
2     █  ~  ~  █  ~  █  █
1     █  ~  ~  █  ~  █  █
 0 1  2  3  4  5  6  7  8  9
```

## The efficient algorithm (two pointers)

Instead of scanning left and right for every block (slow), we use **two
pointers** starting at both ends and move inward. We always move the side with
the **shorter wall**, because that side is the one that decides the water level.

```
left = 0, right = last index
leftMax = 0, rightMax = 0, total = 0

while left < right:
    if height[left] < height[right]:
        if height[left] >= leftMax: leftMax = height[left]
        else: total += leftMax - height[left]
        left++
    else:
        if height[right] >= rightMax: rightMax = height[right]
        else: total += rightMax - height[right]
        right--
```

This finds the answer in **one pass**.

## The calculation code (Vanilla JavaScript)

```js
function trappedWater(heights) {
  let left = 0;
  let right = heights.length - 1;
  let leftMax = 0;
  let rightMax = 0;
  let total = 0;

  while (left < right) {
    if (heights[left] < heights[right]) {
      // The left wall is shorter, so the left side decides the level.
      if (heights[left] >= leftMax) leftMax = heights[left];
      else total += leftMax - heights[left];
      left++;
    } else {
      // The right wall is shorter (or equal).
      if (heights[right] >= rightMax) rightMax = heights[right];
      else total += rightMax - heights[right];
      right--;
    }
  }
  return total;
}

console.log(trappedWater([0, 4, 0, 0, 0, 6, 0, 6, 4, 0])); // 18
```

## Complexity

- **Time:** `O(n)` — a single pass through the array.
- **Space:** `O(1)` — only a few variables.
