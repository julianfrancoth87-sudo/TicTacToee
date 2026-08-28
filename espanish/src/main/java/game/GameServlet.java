package game;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import game.GameBean.GamePlayer;

public class GameServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        GameBean game = (GameBean) request.getSession(true).getAttribute("gameBean");
        
        int line = Integer.parseInt(request.getParameter("Line"));
        int col = Integer.parseInt(request.getParameter("Col"));
        
        game.playPlayerTurn(line, col);
        
        GamePlayer winner = game.getWinner();
        switch(winner){
            case NOBODY:
                if(game.hasEmptyCell()){
                    game.playComputerTurn();
                    switch(game.getWinner()){
                        case NOBODY:
                            break;
                        case COMPUTER:
                            request.setAttribute("winner", "La computadora ha ganado");
                            break;
                        case USER:
                            request.setAttribute("winner", "¡Has ganado!");
                            break;
                    }
                }
                break;
            case COMPUTER:
                request.setAttribute("winner", "La computadora ha ganado");
                break;
            case USER:
                request.setAttribute("winner", "¡Has ganado!");
                break;
        }
        
        // Verificación de empate (si no hay ganador y el tablero está lleno)
        if(game.getWinner() == GamePlayer.NOBODY && !game.hasEmptyCell()){
            request.setAttribute("winner", "¡Empate!");
        }
        
        request.getRequestDispatcher("/game.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}