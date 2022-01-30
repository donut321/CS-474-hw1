object homework1
  enum expressions:
    case Value(input: Any)
    case Variable(name: String)
    case Insert(name: String, op1: Int, op2: String)
    case Assign(name: String, op: Insert)

    def operations: String =
      this match {
        case Variable(name) => name
      }

  @main def test: Unit =
    println("Hello")

