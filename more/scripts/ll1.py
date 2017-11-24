#!/bin/python3

from config import V, T

import logging
import csv

log = logging.getLogger(__name__)
logging.basicConfig(level = logging.DEBUG)

class GrammarException(Exception):
    pass

GRAMMAR_CONFIG = "grammar.py"

def get_grammar_config():
    try:
        config = {}
        exec(open(GRAMMAR_CONFIG).read(), config)
        # FIXME find another way to parse to avoid del builtins
        del config['__builtins__']
        return config
    except FileNotFoundError:
        print("Error: no '%s' file found." % GRAMMAR_CONFIG)
        exit(1)
    except Exception as e:
        log.error("Config Error: ", e)
        exit(1)

class GrammarParser:

    @classmethod
    def _add_in(cls, collection, element):
        for e in collection:
            if element.same(e):
                return;
        log.debug('Adding %s %s' % (element.__class__.__name__, element))
        collection.append(element)


    def __init__(self, grammar, epsilon):
        self.grammar = grammar
        self.epsilon = epsilon
        self.variables = []
        self.terminals = []


    def print_rules(self):
        i = 1
        prleft = ""
        for left in self.grammar:
            right = str()
            for elem in self.grammar[left]:
                right += str(elem) + " "
            nleft = left if str(left) != prleft else ""
            print('{: >5} {: >20} → {: <50}'.format(str(i), str(nleft), right))
            i+=1
            prleft = str(left)

    def add_variable(self, variable):
        """
        Ensure that a variable is added only once
        """
        GrammarParser._add_in(self.variables, variable)

    def add_terminal(self, terminal):
        """
        Ensure that a terminal is added only once
        """
        GrammarParser._add_in(self.terminals, terminal)

    def parse(self):
        for left in self.grammar:
            # leftside is always a var
            self.add_variable(left)
            for elem in self.grammar[left]:
                if isinstance(elem, V):
                    self.add_variable(elem)
                elif isinstance(elem, T):
                    self.add_terminal(elem)
                elif elem == self.epsilon:
                    pass # If epsilon, there is nothing to do
                else:
                    raise GrammarException("Element is not a terminal, " \
                                            "a variable or epsilon, instead " \
                                            "'%s\'" % elem.__class__)
        return {'variables': self.variables, 'terminals': self.terminals}


# Algo 6 from the Lecture Notes and Exercices Session 6
def first_k(grammar, variables, terminals, epsilon, k = 1):
    first = {}
    first[epsilon] = [epsilon]
    rule_processed = {}
    for a in terminals:
        first[str(a)] = [a]
    for A in variables:
        first[str(A)] = None
    for left in grammar:
        rule_processed[left] = False
    stable = False
    while not stable:
        for A in grammar:
            X = str(grammar[A][0])
            if first[X] is not None and not rule_processed[A]:
                log.debug("Adding first of %s in %s" % (X, str(A)))
                log.debug("Which is %s\n" % first[X])
                if first[str(A)] is None:
                    first[str(A)] = []
                first[str(A)].extend(first[X])
                rule_processed[A] = True
        stable = True
        for A in rule_processed:
            stable = stable & rule_processed[A]
        # first(A) = first(A) + directsum1(grammar[X], first)
        # first(A) | directsum1(grammar[A], first, k)
    return first

# def directsum1(righside, first):
#     results = []
#     for X in rightside:
#         if len()
#         if len(result) < k:
#             first(X)




def export_table_template(variables, terminals, csvfile):
    content = [['']]
    for terminal in terminals:
        content[0].append(str(terminal))

    for variable in variables:
        content.append([str(variable)])
    with open(csvfile, "w") as output:
        writer = csv.writer(output, lineterminator='\n')
        writer.writerows(content)

def export_grammar_csv(grammar, variable, terminals, csvfile):
    content = []
    content = [[], []] # First row are variables, second row terminals
    for v in variables:
        content[0].append(v)
    for t in terminals:
        content[1].append(t)
    for left in grammar:
        line = []
        line.append(left)
        for X in grammar[left]:
            line.append(X)
        content.append(line)
    with open(csvfile, "w") as output:
        writer = csv.writer(output, lineterminator='\n')
        writer.writerows(content)

def manual_LL1():
    pass

if __name__ == "__main__":
    config = get_grammar_config()
    grammar = config['grammar']
    parser = GrammarParser(config['grammar'], config['epsilon'])
    print()
    parser.print_rules()
    print()
    data = parser.parse()
    variables, terminals = data['variables'], data['terminals']
    print(data)
    print()
    # first = first_k(config['grammar'], data['variables'], data['terminals'],
    #               config['epsilon'])
    # for i in first:
    #     print("%20s → %s" % (i, first[i]))
    print()
    export_grammar_csv(grammar, variables, terminals, "grammar.csv")
    # export_table_template(data['variables'], data['terminals'], "LL1.csv")
