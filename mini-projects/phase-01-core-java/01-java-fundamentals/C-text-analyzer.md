# Mini-Project C — Text Analyzer (Stretch)

**Phase:** 1 Core Java  
**Topic:** [../../phase-01-core-java/01-java-fundamentals.md](../../phase-01-core-java/01-java-fundamentals.md)  
**Code folder suggestion:** `projects/phase-01/01-java-fundamentals/text-analyzer/`  
**Time:** ~4–6 hours at 1 hr/day  

## Goal

Read a multi-line paragraph until a sentinel, then report **word count**, **character count**, **longest word**, and a basic **word frequency** table. Optional stretch after A + B.

## Prerequisites

- [ ] Comfortable with `String` methods, loops, arrays
- [ ] Mini-projects A and/or B recommended

## Learn (open before coding)

- [`String` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/String.html)
- [Strings (tutorial)](https://docs.oracle.com/javase/tutorial/java/data/strings.html)
- [Arrays](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/arrays.html)
- [`Scanner` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Scanner.html)
- [Packages](https://docs.oracle.com/javase/tutorial/java/package/index.html)

## Project layout

```text
text-analyzer/
  README.md
  src/
    analyzer/
      Main.java
      TextReader.java
      TextStats.java
      WordFrequency.java
```

## Build steps

### Step 1 — Multi-line input until sentinel

- [ ] Create package `analyzer`
- [ ] Prompt: “Enter text; finish with a line containing only `END`”
- [ ] Loop `Scanner.nextLine()` appending lines (with spaces/newlines as you choose) until sentinel
- [ ] Do not include the sentinel in the text
- [ ] Compile/run stub:
  ```bash
  javac -d out src/analyzer/*.java
  java -cp out analyzer.Main
  ```
- **Done when:** Multi-line paste + `END` produces one combined string you can print back.

### Step 2 — Character counts

- [ ] Count total characters including spaces
- [ ] Count characters excluding spaces (optional second metric)
- [ ] Count lines (based on how you joined input)
- [ ] Print all three clearly labeled
- **Done when:** Known sample (“hi\\nthere”) matches expected counts.

### Step 3 — Tokenize words

- [ ] Split on whitespace: `text.trim().split("\\s+")` (handle empty text)
- [ ] If empty input, print zeros and skip later steps safely
- [ ] Store words in a `String[]`
- **Done when:** “one two  three” → 3 words.

### Step 4 — Word count & longest word

- [ ] Word count = length of words array (after empty check)
- [ ] Find longest word (if ties, first or list all—document choice)
- [ ] Ignore punctuation optionally: strip trailing `,`. `!` `?` with a small helper
- **Done when:** Longest word correct on a hand-checked paragraph.

### Step 5 — Word frequency (basic)

- [ ] Use parallel arrays or nested loops: unique words + counts (case-insensitive)
- [ ] Cap unique words (e.g. 200) or document limit
- [ ] Print “word → count” sorted by first-seen order (sorting optional)
- **Done when:** “a a b” → a:2, b:1.

### Step 6 — Menu or single-shot UX

- [ ] Either: one analyze pass then quit, or menu Analyze again / Quit
- [ ] Keep analysis methods out of `main`
- [ ] Add README with sample paragraph and expected stats
- **Done when:** Second analyze run works without restarting JVM if menu used.

### Step 7 — Edge cases

- [ ] Only whitespace → 0 words
- [ ] Single word, no spaces
- [ ] Very long line
- [ ] Words differing only by case count as same for frequency
- **Done when:** No crashes on these cases.

### Step 8 — Optional stretch polish

- [ ] Top-N frequent words
- [ ] Average word length
- [ ] Character frequency for letters a–z
- **Done when:** At least one stretch metric printed (or explicitly skipped).

## Definition of Done

- [ ] Multi-line input until sentinel works
- [ ] Word count, character count, longest word printed
- [ ] Basic word frequency printed
- [ ] Empty / whitespace-only input handled
- [ ] Package + README present

## Demo script

1. Enter:
   ```text
   The cat sat on the mat
   The cat
   END
   ```
2. Expect word count ≥ 8 (depending on join), characters > 0
3. Longest word likely `The`/`cat`/`sat`/`mat` (after normalize) — verify your rules
4. Frequency: `the` and `cat` appear more than once (case-insensitive)

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Including `END` in text | Break before appending sentinel |
| `split` on empty → one empty token | Guard `trim().isEmpty()` first |
| Frequency with `==` | Use `equalsIgnoreCase` |
| Crashing on no words when finding longest | Check length before loop |
| Mutating while iterating carelessly | Build unique list carefully |

## After this

Return to [01-java-fundamentals.md](../../phase-01-core-java/01-java-fundamentals.md) §12 mastery gate · Next topic: [02-oops.md](../../phase-01-core-java/02-oops.md)
