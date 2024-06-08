package filtro;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entidade.Login;

public class loginAuth implements Filter
{

   public loginAuth()
   {
   }

   public void destroy()
   {
   }

   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
   {
      HttpServletRequest req = (HttpServletRequest)request;
      HttpServletResponse res = (HttpServletResponse)response;
      HttpSession session = (HttpSession)req.getSession();
      
      Login login = (Login)session.getAttribute("usuario");
      
      if(Objects.isNull(login))
      {
         chain.doFilter(request, response);
      }
      else
      {
         res.sendRedirect(req.getContextPath() + "/app/navegacao.xhtml");
      }
   }

   public void init(FilterConfig fConfig) throws ServletException
   {
   }

}
