package bean;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@ManagedBean
@SessionScoped
public class HomeBean
{

   public void download() throws IOException, DocumentException
   {
      byte[] pdfBytes = gerarRelatorioPDF();
      salvarPDF(pdfBytes);
   }

   private void salvarPDF(byte[] pdfBytes) throws IOException
   {
      try (FileOutputStream fileOutputStream = new FileOutputStream("c:/relatorio/relatorio.pdf"))
      {
         fileOutputStream.write(pdfBytes);
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Download feito com sucesso"));
      }
      catch (IOException e)
      {
         FacesContext.getCurrentInstance().addMessage(null,
               new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao realizar o download", ""));
         throw e;
      }
   }

   private byte[] gerarRelatorioPDF() throws DocumentException, IOException
   {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      Document document = new Document();
      PdfWriter.getInstance(document, outputStream);

      document.open();
      adicionarConteudo(document);
//      adicionarDiagramaUML(document);
      document.close();

      return outputStream.toByteArray();
   }

   private void adicionarConteudo(Document document) throws DocumentException
   {
      Font fontNegrito = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
      Paragraph titulo = new Paragraph("PROJETO INTEGRADOR - ENGENHARIA DE SOFTWARE", fontNegrito);
      document.add(titulo);

      document.add(new Paragraph(" "));
      document.add(new Paragraph("Sobre"));
      document.add(new Paragraph(" "));
      document.add(new Paragraph("Olá! Seja bem vindo ao sistema de agendamentos desenvolvido por nossa equipe."));
      document.add(new Paragraph("Esse é um sistema para agendamentos desenvolvido utilizando Java, JSF e Primefaces."));
      document.add(new Paragraph(
            "O projeto tem como objetivo principal abordar os conceitos de aplicações SPAs, Autenticação e Autorização com Tokens JWT e muito mais."));
      document.add(new Paragraph("O código fonte do projeto pode ser acessado clicando no link Github no menu lateral."));
      document.add(new Paragraph(" "));
      document.add(new Paragraph("Integrantes:"));
      document.add(new Paragraph(" "));
      document.add(new Paragraph("Igor Ribeiro"));
      document.add(new Paragraph("Lucas Silva"));
      document.add(new Paragraph("Petrus"));
   }

   /*
    * private void adicionarDiagramaUML(Document document) throws
    * DocumentException, IOException { String diagramaUML = "@startuml\n" +
    * "ClassDiagram\n" + "class Usuario\n" + "@enduml"; SourceStringReader
    * reader = new SourceStringReader(diagramaUML); FileFormatOption fileFormat
    * = new FileFormatOption(FileFormat.PNG); ByteArrayOutputStream outputStream
    * = new ByteArrayOutputStream(); reader.generateImage(outputStream,
    * fileFormat); com.itextpdf.text.Image img =
    * com.itextpdf.text.Image.getInstance(outputStream.toByteArray());
    * document.add(img); }
    */

}