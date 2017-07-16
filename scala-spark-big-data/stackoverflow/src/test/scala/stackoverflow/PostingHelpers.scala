package stackoverflow

trait PostingHelpers {
  private var lastId = 10000
  private def nextId: Int = {
    lastId = lastId + 1
    lastId
  }

  def answer(question: Int, score: Int): Posting = Posting(2, nextId, None, Option(question), score, None)
  def question(id: Int, language: String, score: Int): Posting = Posting(1, id, None, None, score, Option(language))
}
