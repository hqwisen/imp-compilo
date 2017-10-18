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

A regular expression can only be matched when its associated set of lexical states includes
the currently active lexical state of the scanner or if the set of associated lexical states is
empty and the currently active lexical state is inclusive. Exclusive and inclusive states
only differ at this point: rules with an empty set of associated states.

### CODE

When in CODE we need to know if we just began to process the block


## Regexp order

The order of the regex in YYINITIAL are important. For example VarName
is a the end because if set, for example, before end, there are two matches
if the token end is detected, 'VarName' and 'end' and regex are (matched) and the same length
so the first one will be used.