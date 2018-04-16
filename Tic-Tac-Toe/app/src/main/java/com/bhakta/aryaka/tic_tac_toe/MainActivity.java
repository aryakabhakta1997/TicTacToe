package com.bhakta.aryaka.tic_tac_toe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button[][] buttons = new Button[3][3];

    private boolean playerTurn = true;

    private int roundCount;

    private int playerPoints;
    private int computerPoints;

    private TextView textViewplayer;
    private TextView textViewcomputer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });

        textViewplayer = findViewById(R.id.text_view_p1);
        textViewcomputer = findViewById(R.id.text_view_p2);

        for (int i=0;i<3;i++)
        {
            for (int j=0;j<3;j++)
            {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (!((Button)v).getText().toString().equals(""))
        {
            Toast.makeText(this,"Please select an empty cell",Toast.LENGTH_SHORT);
            return;
        }
        else if(playerTurn)
        {
            ((Button)v).setText("X");
        }
        else
        {
            ((Button)v).setText("O");
        }
        roundCount++;
        Thread t=new Thread() {

            @Override
            public void run()
            {
                if(checkForWin())
                {
                    if (playerTurn)
                        playerWins();
                    else
                        computerWins();
                }
                else if (roundCount == 9)
                    draw();
                else
                    playerTurn = !playerTurn;
            }
        };
        t.start();
    }

    private boolean checkForWin() {
        String[][] field = new String[3][3];

        for(int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                field[i][j] = buttons[i][j].getText().toString();

        for (int i = 0; i < 3; i++)
            if (field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals(""))
                return true;

        for (int i = 0; i < 3; i++)
            if (field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals(""))
                return true;

        if (field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals(""))
            return true;

        if (field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].equals(""))
            return true;

        return false;
    }

    private void playerWins()
    {
        playerPoints++;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Player 1 wins!", Toast.LENGTH_SHORT).show();
                updatePointsText();
                resetBoard();
            }
        });
    }

    private void computerWins()
    {
        computerPoints++;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Computer wins!", Toast.LENGTH_SHORT).show();
                updatePointsText();
                resetBoard();
            }
        });
    }

    private void draw()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Draw!", Toast.LENGTH_SHORT).show();
            }
        });
        try {
            sleep(700);
        }catch (Exception e){
            e.printStackTrace();
        }
        resetBoard();
    }

    private void updatePointsText()
    {
        textViewplayer.setText("Player 1: " + playerPoints);
        textViewcomputer.setText("Computer: " + computerPoints);
        try {
            sleep(700);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void resetBoard()
    {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                buttons[i][j].setText("");

        roundCount = 0;
        playerTurn = true;
    }

    private void resetGame()
    {
        computerPoints=playerPoints = 0;
        updatePointsText();
        resetBoard();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("roundCount", roundCount);
        outState.putInt("playerPoints", playerPoints);
        outState.putInt("computerPoints", computerPoints);
        outState.putBoolean("playerTurn", playerTurn);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        roundCount = savedInstanceState.getInt("roundCount");
        playerPoints = savedInstanceState.getInt("playerPoints");
        computerPoints = savedInstanceState.getInt("computerPoints");
        playerTurn = savedInstanceState.getBoolean("playerTurn");
    }
}