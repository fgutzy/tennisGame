package com.example.demo.views;


import com.example.demo.entity.Player;
import com.example.demo.service.LogInService;
import com.example.demo.service.PlayerService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;


@Route("/leaderboard")
//@PermitAll
@PageTitle("Leaderboard")

public class LeaderboardView extends VerticalLayout {

  Grid<Player> grid = new Grid<>(Player.class);
  TextField filteredText = new TextField();

  private PlayerService playerService;

  //@Autowired
  LogInService logInService;



  public LeaderboardView(PlayerService playerService, LogInService logInService) {
    this.playerService = playerService;
    this.logInService = logInService;
    addClassName("Score-view");
    setSizeFull();
    configureGrid();
    configureFilter();



    Button loginLogoutButton = new Button();
    if (!logInService.isPlayerOneLoggedIn()){
      loginLogoutButton.setText("Log In");
    }else loginLogoutButton.setText("Log Out");

    loginLogoutButton.addClickListener(event -> {
      try {
        Thread.sleep(1500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      UI.getCurrent().navigate("/login");
        logInService.setNameOfLoggedInUserOne("");
        logInService.setNameOfLoggedInUserTwo("");
        logInService.setPlayerOneLoggedIn(false);
        logInService.setPlayerTwoLoggedIn(false);
    });


    HorizontalLayout header = new HorizontalLayout(loginLogoutButton);

    add(header, filteredText, grid);

    updateList();

  }

  private void configureFilter() {
    filteredText.setPlaceholder("Filter for Player");
    filteredText.setClearButtonVisible(true);
    filteredText.setValueChangeMode(ValueChangeMode.LAZY);
    filteredText.addValueChangeListener(e -> updateList());
  }


  private void updateList() {
    //sets Items that either contain a filtered value or whole List
    grid.setItems(playerService.findAll(filteredText.getValue()));
  }


  private void configureGrid() {
    grid.addClassName("score-grid");
    grid.setSizeFull();
    grid.setColumns("name", "gamesWon", "gamesLost");
    grid.addColumn(Player::getGamesPlayed).setHeader("gamesPlayed");
  }

}
