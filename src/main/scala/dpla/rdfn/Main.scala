package dpla.rdfn

import java.io.{File, FileOutputStream, FilenameFilter}

import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.riot.{Lang, RDFDataMgr}

import scala.util.{Failure, Success, Try}

object Main {

  def main(args: Array[String]): Unit = {
    val in = args(0)
    val out = args(1)
    val model = ModelFactory.createDefaultModel()
    val files = new File(in).listFiles(new FilenameFilter {
      override def accept(dir: File, name: String): Boolean = name.endsWith(".rdf")
    })

    // Read each .rdf file and add contents to model
    files.foreach(file => {
      println(s"Reading from ${file.toString}")
      model.read(file.toString)
    })

    // serialize model in nested RDFXML
    Try {
      RDFDataMgr.write(new FileOutputStream(out), model, Lang.RDFXML)
    } match {
      case Success(s) => println(s"Nested output written to $out")
      case Failure(f) => println(s"Unable to write output\n${f.getMessage}")
    }

    println("Finished.")
  }
}
