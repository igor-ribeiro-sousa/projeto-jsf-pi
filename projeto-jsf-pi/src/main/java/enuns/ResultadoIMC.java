package enuns;

public enum ResultadoIMC {
   MUITO_ABAIXO_DO_PESO(Double.MIN_VALUE, 17),
   ABAIXO_DO_PESO(17, 18.5),
   PESO_NORMAL(18.5, 25),
   ACIMA_DO_PESO(25, 30),
   OBESIDADE_GRAU_1(30, 35),
   OBESIDADE_GRAU_2(35, 40),
   OBESIDADE_GRAU_3(40, Double.MAX_VALUE);

   private final double min;
   private final double max;

   ResultadoIMC(double min, double max)
   {
      this.min = min;
      this.max = max;
   }

   public static ResultadoIMC calcularIMC(double imc)
   {
      for (ResultadoIMC resultado : values())
      {
         if (imc >= resultado.min && imc < resultado.max)
         {
            return resultado;
         }
      }
      throw new IllegalArgumentException("IMC fora do intervalo esperado: " + imc);
   }
}
