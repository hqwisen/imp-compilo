# Derivation Tree

Build the derivation tree during the stack procedure. A second
stack is used with Nodes in it. Before poping a variable, 
add children base on the rule and push them on the stack.

# Code generation

We don't have function in our imp lang. This means that everything
can be declared in the same scope (i.e. the main).

# Section report

* treeStack -> ParseTree -> AST