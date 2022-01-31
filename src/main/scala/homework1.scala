
object homework1:
  private type typ = String | Int
  enum expressions:
    case Value(input: typ)
    case Variable(name: String)
    case Insert(name: Int, op1: typ, op2: typ)
    case Assign(name: String, op: Set[typ])
    private val map: Map[String, Set[typ]] = Map()

    def eval: typ | Set[typ] =
      this match {
        case Value(op) => op
        case Insert(op1, op2, op3) => Set(op1, op2, op3)
        case Assign(op1, op2) => map(op1, op2.eval)
      }

  @main def test(): Unit =
    import expressions.*

    println(Value("String").eval)

