# imp-compilo


## Quick notes

Use states to switch grammar rules.

## Switching between grammar rules

We can eithert use {Blank} to detect tokens or use yypushback
to re-read the input when changing the state.

## Blank purpose

We need blank between tokens because

**begin end** is correct but **beginend** in not

The regex \s match also then new line characters

## Nested comments

Use the same idea from C++, Java

Changing states ?

## States

### CODE

When in CODE we need to know if we just began to process the block
