package bean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import entidade.IMC;

@ManagedBean
@SessionScoped
public class ImcBean
{
   private IMC imc;
   private String nomePacientePesquisa;
   private List<IMC> imcs;
   private List<IMC> listaResultado;
   private boolean exibirResultadosPesquisa;
   
   @ManagedProperty(value = "#{navegacaoBean}")
   private NavegacaoBean navegacaoBean;
   
   public ImcBean()
   {
      this.imc = new IMC();
      this.imcs = new ArrayList<IMC>();
      this.listaResultado = new ArrayList<IMC>();
      this.exibirResultadosPesquisa = false;
   }


   public IMC getImc()
   {
      return imc;
   }

   public void setImc(IMC imc)
   {
      this.imc = imc;
   }

   public String getNomePacientePesquisa()
   {
      return nomePacientePesquisa;
   }

   public void setNomePacientePesquisa(String nomePacientePesquisa)
   {
      this.nomePacientePesquisa = nomePacientePesquisa;
   }

   public List<IMC> getImcs()
   {
      return imcs;
   }

   public void setImcs(List<IMC> imcs)
   {
      this.imcs = imcs;
   }

   public List<IMC> getListaResultado()
   {
      return listaResultado;
   }

   public void setListaResultado(List<IMC> listaResultado)
   {
      this.listaResultado = listaResultado;
   }

   public boolean isExibirResultadosPesquisa()
   {
      return exibirResultadosPesquisa;
   }

   public void setExibirResultadosPesquisa(boolean exibirResultadosPesquisa)
   {
      this.exibirResultadosPesquisa = exibirResultadosPesquisa;
   }

   public NavegacaoBean getNavegacaoBean()
   {
      return navegacaoBean;
   }

   public void setNavegacaoBean(NavegacaoBean navegacaoBean)
   {
      this.navegacaoBean = navegacaoBean;
   }

}
