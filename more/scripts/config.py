
class BasicElement:
    def __init__(self, name):
        self.name = name

    def same(self, other):
        if isinstance(other, BasicElement):
            return self.name == other.name
        return False

    def __str__(self):
        return self.name

    def __repr__(self):
        return str(self)

class V(BasicElement):

    def __str__(self):
        return '<%s>' % str(self.name)

class T(BasicElement):
    pass
