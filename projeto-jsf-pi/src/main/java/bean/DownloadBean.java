package bean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import util.Util;

@ManagedBean
@SessionScoped
public class DownloadBean
{

   public void download() throws IOException, DocumentException
   {
      byte[] pdfBytes = gerarRelatorioPDF();
      salvarPDF(pdfBytes);
   }

   private void salvarPDF(byte[] pdfBytes) throws IOException
   {
      String diretorio = System.getProperty("user.home") + File.separator + "relatorio";
      File diretorioRelatorio = new File(diretorio);
      if (!diretorioRelatorio.exists())
      {
         diretorioRelatorio.mkdirs();
      }

      String caminhoRelatorio = diretorio + File.separator + "relatorio.pdf";

      try (FileOutputStream fileOutputStream = new FileOutputStream(caminhoRelatorio))
      {
         fileOutputStream.write(pdfBytes);
         Util.addMensagemInfo("Download feito com sucesso");
      }
      catch (IOException e)
      {
         Util.addMensagemErro("Erro ao realizar o download");
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
      document.add(new Paragraph("Olá e seja muito bem-vindo ao projeto da disciplina de Projeto Integrador! Neste trabalho, estamos embarcando em uma jornada para desenvolver um sistema de agendamento de consultas, uma ferramenta essencial para otimizar a gestão de compromissos e impulsionar a eficiência em diversos setores."));
      document.add(new Paragraph("Utilizando uma poderosa combinação de tecnologias de ponta, como Java, JSF e Primefaces, estamos construindo uma plataforma robusta e intuitiva para atender às demandas. Além disso, integramos funcionalidades CRUD (Create, Read, Update, Delete) para garantir uma gestão eficaz e abrangente dos dados."));
      document.add(new Paragraph("Este projeto vai além da simples criação de um aplicativo funcional; é uma exploração dos conceitos fundamentais por trás das aplicações de página única (SPAs), Autenticação e Autorização. Estamos mergulhando fundo no JSF, explorando alguns detalhes do HTML, CSS e JavaScript para criar uma experiência de usuário envolvente e dinâmica."));
      document.add(new Paragraph("Além disso, estamos comprometidos com as melhores práticas de programação, buscando um código limpo, organizado e escalável. Queremos entregar não apenas um produto funcional, mas também um código que seja fácil de compreender, manter e expandir no futuro."));
      document.add(new Paragraph(" "));
      document.add(new Paragraph("Integrantes:"));
      List listIntegrantes = new List(List.UNORDERED);
      listIntegrantes.add(new ListItem("Antonio Igor Ribeiro"));
      listIntegrantes.add(new ListItem("Lucas Silva"));
      listIntegrantes.add(new ListItem("Petrus"));
      document.add(listIntegrantes);
   }

}