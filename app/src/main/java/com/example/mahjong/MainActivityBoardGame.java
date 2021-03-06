package com.example.mahjong;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import Controller.GameController;
import Core.BoardGame;
import Core.BoardPlayer;

/**
 * This class represent an abstract activity for a board game
 * @author Mano Brabant
 * @author Benjamin Riviere
 * @version 1.0
 */
public abstract class MainActivityBoardGame extends AppCompatActivity implements View.OnClickListener {

    //Model & Controller
    protected BoardGame boardGame;
    protected GameController controller;

    //Elements of the view
    protected Button[][] buttons;
    protected TextView textViewPlayer1;
    protected TextView textViewPlayer2;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        textViewPlayer1 = findViewById(R.id.text_view_p1);
        textViewPlayer2 = findViewById(R.id.text_view_p2);

        this.buttons = new Button[this.boardGame.getGrid().getNbRow()][this.boardGame.getGrid().getNbCol()];

        for (int i = 0; i < this.boardGame.getGrid().getNbRow(); i++) {
            for (int j = 0; j < this.boardGame.getGrid().getNbCol(); j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }

        Animation scaleUp, scaleDown;

        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        @SuppressLint("WrongViewCast") Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnTouchListener((v, event) -> {
            if(event.getAction()== MotionEvent.ACTION_DOWN){
                buttonReset.startAnimation(scaleUp);
            }else if(event.getAction()==MotionEvent.ACTION_UP){
                buttonReset.startAnimation(scaleDown);
                clickReset();
            }
            return true;
        });

        @SuppressLint("WrongViewCast") Button buttonRetry = findViewById(R.id.button_retry);
        buttonRetry.setOnTouchListener((v, event) -> {
            if(event.getAction()== MotionEvent.ACTION_DOWN){
                buttonRetry.startAnimation(scaleUp);
            }else if(event.getAction()==MotionEvent.ACTION_UP){
                buttonRetry.startAnimation(scaleDown);
                clickRetry();
            }
            return true;
        });

        this.update();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * This method return the row of a given Button
     * @param id Le id of the button
     * @return The row's number of the button
     */
    public int getRow(int id) {
        return Integer.parseInt(getResources().getResourceEntryName(id).split("_")[1].substring(0,1));
    }


    /**
     * This method return the column of a given Button
     * @param id Le id of the button
     * @return The column's number of the button
     */
    public int getCol(int id) {
        return Integer.parseInt(getResources().getResourceEntryName(id).split("_")[1].substring(1,2));
    }


    /**
     * This method reset the game
     */
    private void clickReset() {

        this.controller.clickReset();
        this.update();

    }


    /**
     * This method restart a new game
     */
    private void clickRetry() {

        this.controller.clickRetry();
        this.update();

    }


    @Override
    public void onClick(View v) {

        this.controller.clickGrid(this.getRow(v.getId()), this.getCol(v.getId()));
        this.update();

    }


    /**
     * This method update the view.
     * It take the info needed in the model to update the view
     */
    @SuppressLint("SetTextI18n")
    public void update() {

        for (int i = 0; i < this.boardGame.getGrid().getNbRow(); i++) {
            for (int j = 0; j < this.boardGame.getGrid().getNbCol(); j++) {
                if(this.boardGame.getGrid().getCell(i, j).getPlayer().equals(BoardPlayer.NONE)) {
                    buttons[i][j].setText("");
                } else {
                    buttons[i][j].setText(this.boardGame.getGrid().getCell(i, j).getPlayer().toString());
                }
            }
        }

        this.textViewPlayer1.setText(getString(R.string.player1) + " : " + this.boardGame.getPlayer1Points());
        this.textViewPlayer2.setText(getString(R.string.player2) + " : " + this.boardGame.getPlayer2Points());

    }


    /**
     * This method show a pop up saying that the player1 has wined
     */
    public void player1Wins() {
        Toast.makeText(this, getString(R.string.player1Wins), Toast.LENGTH_SHORT).show();
    }


    /**
     * This method show a pop up saying that the player2 has wined
     */
    public void player2Wins() {
        Toast.makeText(this, getString(R.string.player2Wins), Toast.LENGTH_SHORT).show();
    }


    /**
     * This method show a pop up saying that the game is a draw
     */
    public void draw() {
        Toast.makeText(this, getString(R.string.draw), Toast.LENGTH_SHORT).show();
    }

}