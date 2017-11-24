# Defined grammar for our IMP language
# Variables are surrounded by V('variable')
# and terminals by T('terminal'). Variable and Terminals are case sensitive.
# Epsilon: note that epsilon shouldn't be the same symbol as any terminals
# The rules indices, will be generated in the given order, starting at 1.
# The grammar should be the transformed grammar.

from config import V, T

epsilon = "epsilon"

grammar = {
    V('Program'): [T('begin'), V('Code'), T('end')],
    V('Code'): [V('InstList')],
    V('Code'): [epsilon],
    V('InstList'): [V('Instruction'), V('InstListSeq')],
    V('InstListSeq'): [T(';'), V('InstList')],
    V('InstListSeq'): [epsilon],
    V('Instruction'): [V('Assign')],
    V('Instruction'): [V('If')],
    V('Instruction'): [V('While')],
    V('Instruction'): [V('For')],
    V('Instruction'): [V('Print')],
    V('Instruction'): [V('Read')],
    V('Assign'): [T('[VarName]'), T(':='), V('ExprArith')],
    V('ExprArith'): [V('ExprProd'), V('ExprArithPrime')],
    V('ExprArithPrime'): [V('SumSubOp'), V('ExprProd'), V('ExprArithPrime')],
    V('ExprArithPrime'): [epsilon],
    V('ExprProd'): [V('Atom'), V('ExprProdPrime')],
    V('ExprProdPrime'): [V('ProdOp'), V('Atom'), V('ExprProdPrime')],
    V('ExprProdPrime'): [epsilon],
    V('SumSubOp'): [T('+')],
    V('SumSubOp'): [T('-')],
    V('ProdOp'): [T('*')],
    V('ProdOp'): [T('/')],
    V('Atom'): [T('[VarName]')],
    V('Atom'): [T('[Number]')],
    V('Atom'): [T('-'), V('Atom')],
    V('Atom'): [T('('), V('ExprArith'), T(')')],
    V('If'): [T('if'), V('Cond'), T('then'), V('Code'), V('IfSeq')],
    V('IfSeq'): [T('endif')],
    V('IfSeq'): [T('else'), V('Code'), T('endif')],
    V('Cond'): [V('ConjCond'), V('CondPrime')],
    V('CondPrime'): [T('or'), V('ConjCond'), V('CondPrime')],
    V('CondPrime'): [epsilon],
    V('ConjCond'): [V('AtomCond'), V('ConjCondPrime')],
    V('ConjCondPrime'): [T('and'), V('AtomCond'), V('ConjCondPrime')],
    V('ConjCondPrime'): [epsilon],
    V('AtomCond'): [V('SimpleCond')],
    V('AtomCond'): [T('not'), V('SimpleCond')],
    V('SimpleCond'): [V('ExprArith'), V('Comp'), V('ExprArith')],
    V('Comp'): [T('=')],
    V('Comp'): [T('>=')],
    V('Comp'): [T('>')],
    V('Comp'): [T('<=')],
    V('Comp'): [T('<')],
    V('Comp'): [T('<>')],
    V('While'): [T('while'), V('Cond'), T('do'), V('Code'), T('done')],
    V('For'): [T('for'), T('[VarName]'), T('from'), V('ExprArith'), V('ForOp'),
               T('to'), V('ExprArith'), T('do'), V('Code'), T('done')],
    V('ForOp'): [T('by'), V('ExprArith')],
    V('ForOp'): [epsilon],
    V('Print'): [T('print'), T('('), T('[VarName]'), T(')')],
    V('Read'): [T('read'), T('('), T('[VarName]'), T(')')],

}
