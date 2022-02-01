
object homework1:
  private type typ = String | Int | Boolean
  enum expressions:
    case Value(input: typ)
    case Variable(name: String)
    case Insert(name: expressions, op1: expressions, op2: expressions)
    case Assign(name: expressions, op: expressions)
    case Print(op: String)
    case Check(name: String, value: expressions)
    private val map: Map[String,Set[typ]] = Map()

    def eval: typ | Set[typ] =
      this match {
        case Variable(op) => op
        case Value(op) => op
        case Insert(op1, op2, op3) => Set(op1.eval, op2.eval, op3.eval)
        case Assign(op1, op2) => map(op1.eval).union(op2.eval)
        case Print(op1) => map(op1) //!! Temporary.. Delete later
        case Check(op1, op2) => map.exists(op1).contains(op2)
      }

  @main def test(): Unit =
    import expressions.*
    Assign(Variable("someSetName"), Insert(Variable("var"), Value(1), Value("somestring")))

    println(Print("someSetName"))

