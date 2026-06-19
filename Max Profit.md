# Max Profit Problem — Solution

## The problem in simple words

Mr. X has unlimited land. He can build three kinds of buildings. Each building
takes some **time to build**, and after it is finished it **earns money for
every remaining unit of time**.

| Building          | Build time | Earns per time unit |
| ----------------- | ---------- | ------------------- |
| Theatre (T)       | 5          | $1500               |
| Pub (P)           | 4          | $1000               |
| Commercial Park (C) | 10       | $2000               |

Rules:
- He can build **only one building at a time** (no two in parallel).
- He has `n` total units of time.
- We must find the mix of buildings (how many T, P, C) that makes the **most money**.

## The key idea (how earning works)

A building only earns money **after** it is built, for the time that is left.

> If a building finishes at time `f`, it stays operational until time `n`,
> so it earns `rate × (n − f)`.

Because we build one after another, the building you start **first** earns over
a longer period. So it is always better to finish expensive-but-profitable
buildings as early as possible.

### Example (n = 13, two Theatres)

```
Time:   0 ----5---- 10 -13
        build T1   build T2
```

- Theatre 1 finishes at time 5 → operates for 13 − 5 = **8** units → 8 × 1500 = $12,000
- Theatre 2 finishes at time 10 → operates for 13 − 10 = **3** units → 3 × 1500 = $4,500
- **Total = $16,500** ✅

## The algorithm (simple recursion / DP)

Think of it like this. If `f(t)` = the most money you can make with `t` units of
time available, then for the **first** building you pick (cost `c`, rate `r`):

```
f(t) = max over each building b that fits (c ≤ t) of:
           r × (t − c)      ← money this building earns in the leftover time
         + f(t − c)         ← best you can do with the remaining time
```

And `f(t) = 0` when no building fits (`t < 4`).

This is a 1‑dimensional dynamic programming problem. We also keep track of
**which** building gave the best result so we can reconstruct the mix at the end.

## Why a tie can happen (n = 7)

- Theatre: 1500 × (7 − 5) = **$3000**
- Pub: 1000 × (7 − 4) = **$3000**

Both give the same money, so there are **two valid solutions**. The code below
reports **all** combinations that hit the maximum.

## Solution code (JavaScript)

```js
const BUILDINGS = [
  { name: "T", label: "Theatre", cost: 5, rate: 1500 },
  { name: "P", label: "Pub", cost: 4, rate: 1000 },
  { name: "C", label: "Commercial Park", cost: 10, rate: 2000 },
];

// Returns the maximum earnings and every building-mix that achieves it.
function maxProfit(n) {
  // best[t] = maximum money achievable with t units of time.
  const best = new Array(n + 1).fill(0);

  for (let t = 1; t <= n; t++) {
    for (const b of BUILDINGS) {
      if (b.cost <= t) {
        const earned = b.rate * (t - b.cost) + best[t - b.cost];
        if (earned > best[t]) best[t] = earned;
      }
    }
  }

  const maxEarnings = best[n];

  // Reconstruct every combination of counts that reaches maxEarnings.
  const solutions = [];
  const seen = new Set();

  function build(t, counts) {
    if (best[t] === 0) {
      // No more profitable building fits. Record this combination.
      const key = `${counts.T}-${counts.P}-${counts.C}`;
      if (!seen.has(key)) {
        seen.add(key);
        solutions.push({ ...counts });
      }
      return;
    }
    for (const b of BUILDINGS) {
      if (b.cost <= t) {
        const earned = b.rate * (t - b.cost) + best[t - b.cost];
        if (earned === best[t]) {
          counts[b.name]++;
          build(t - b.cost, counts);
          counts[b.name]--; // backtrack
        }
      }
    }
  }

  build(n, { T: 0, P: 0, C: 0 });

  return { maxEarnings, solutions };
}

// --- Test cases from the assignment ---
for (const n of [7, 8, 13]) {
  const { maxEarnings, solutions } = maxProfit(n);
  console.log(`Time Unit: ${n}  ->  Earnings: $${maxEarnings}`);
  solutions.forEach((s, i) =>
    console.log(`  ${i + 1}. T: ${s.T} P: ${s.P} C: ${s.C}`)
  );
}
```

### Expected output

```
Time Unit: 7  ->  Earnings: $3000
  1. T: 1 P: 0 C: 0
  2. T: 0 P: 1 C: 0
Time Unit: 8  ->  Earnings: $4500
  1. T: 1 P: 0 C: 0
Time Unit: 13  ->  Earnings: $16500
  1. T: 2 P: 0 C: 0
```

This matches all three test cases. ✅

## Complexity

- **Time:** `O(n × 3)` = `O(n)` for the DP table (3 building types).
- **Space:** `O(n)` for the `best` array.
```