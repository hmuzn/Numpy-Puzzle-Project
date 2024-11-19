package com.example.number_puzzle_project;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    private Button[][] buttons = new Button[3][3];
    private int[][] puzzleArray = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}}; // 초기 퍼즐 상태
    private final int EMPTY_VALUE = 0; // 빈 공간을 나타내는 값
    private Button BtnReset, BtnEnd;
    private int ClickCount = 0;
    private boolean isCleared = false;
    private long startTimeMillis; // 시작 시간을 저장하는 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);

        initializeButtons();
        shufflePuzzle();

        // 게임이 시작될 때 시작 시간 설정
        startTimeMillis = System.currentTimeMillis();

        // 각 버튼에 클릭 리스너 추가
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                final int row = i;
                final int col = j;
                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onButtonClick(row, col);
                    }
                });
            }
        }
        setButtonClickListeners();
    }

    private void initializeButtons() {
        buttons[0][0] = findViewById(R.id.Btn1);
        buttons[0][1] = findViewById(R.id.Btn2);
        buttons[0][2] = findViewById(R.id.Btn3);
        buttons[1][0] = findViewById(R.id.Btn4);
        buttons[1][1] = findViewById(R.id.Btn5);
        buttons[1][2] = findViewById(R.id.Btn6);
        buttons[2][0] = findViewById(R.id.Btn7);
        buttons[2][1] = findViewById(R.id.Btn8);
        buttons[2][2] = findViewById(R.id.Btn9);

        BtnReset = findViewById(R.id.BtnReset);
        BtnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResetPuzzle();
            }
        });

        BtnEnd = findViewById(R.id.BtnEnd);
        BtnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void shufflePuzzle() {
        List<Integer> flatList = flattenArray();
        Collections.shuffle(flatList);

        int index = 0;
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                int value = flatList.get(index++);
                buttons[i][j].setText(value == EMPTY_VALUE ? "" : String.valueOf(value));
                puzzleArray[i][j] = value;

            }
        }
        disableButtons();
    }

    private List<Integer> flattenArray() {
        List<Integer> flatList = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            flatList.add(i);
        }
        flatList.add(EMPTY_VALUE);
        return flatList;
    }

    private void onButtonClick(int row, int col) {
        if (!isCleared) {
            int[] emptySpace = findEmptySpace();

            if (emptySpace != null && isAdjacent(row, col, emptySpace[0], emptySpace[1])) {
                buttons[emptySpace[0]][emptySpace[1]].setText(puzzleArray[row][col] == EMPTY_VALUE ? "" : String.valueOf(puzzleArray[row][col]));
                buttons[row][col].setText("");
                puzzleArray[emptySpace[0]][emptySpace[1]] = puzzleArray[row][col];
                puzzleArray[row][col] = EMPTY_VALUE;

                ClickCount++;
                TextView countTextView = findViewById(R.id.Count);
                countTextView.setText("횟수: " + ClickCount);

                if (isPuzzleComplete()) {
                    // 경과 시간 계산
                    long endTimeMillis = System.currentTimeMillis();
                    long elapsedTimeMillis = endTimeMillis - startTimeMillis;

                    // 경과 시간을 초로 변환 (선택 사항)
                    long elapsedSeconds = elapsedTimeMillis / 1000;

                    // 경과 시간을 토스트 메시지로 표시 (필요에 따라 사용)
                    Toast.makeText(this, "클리어!! 시간: " + elapsedSeconds + " 초", Toast.LENGTH_SHORT).show();

                    // 게임 클리어 결과를 팝업 메시지로 표시
                    showClearResultPopup(elapsedSeconds);

                    disableButtons();
                }
            }
        }
    }

    private int[] findEmptySpace() {
        for (int i = 0; i < puzzleArray.length; i++) {
            for (int j = 0; j < puzzleArray[i].length; j++) {
                if (puzzleArray[i][j] == EMPTY_VALUE) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    private boolean isAdjacent(int row1, int col1, int row2, int col2) {
        return Math.abs(row1 - row2) + Math.abs(col1 - col2) == 1;
    }

    private boolean isPuzzleComplete() {
        int count = 1;
        for (int i = 0; i < puzzleArray.length; i++) {
            for (int j = 0; j < puzzleArray[i].length; j++) {
                if (i == puzzleArray.length - 1 && j == puzzleArray[i].length - 1) {
                    if (puzzleArray[i][j] != EMPTY_VALUE) {
                        return false;
                    }
                } else {
                    if (puzzleArray[i][j] != count) {
                        return false;
                    }
                    count++;
                }
            }
        }
        isCleared = true;
        return true;
    }

    private void setButtonClickListeners() {
        // BtnReset 버튼에 대한 클릭 리스너 설정
        BtnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResetPuzzle();
            }
        });

        // BtnEnd 버튼에 대한 클릭 리스너 설정
        BtnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 종료 버튼을 눌렀을 때 종료 여부를 묻는 팝업 표시
                showHomeConfirmationPopup();
            }
        });
    }

    private void showHomeConfirmationPopup() {
        // 종료 여부를 묻는 팝업 메시지 표시
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("뒤로 가기");
        builder.setMessage("메인 화면으로 이동하시겠습니까?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 예 버튼을 눌렀을 때 앱 종료
                finish();
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 아니오 버튼을 눌렀을 때 아무 동작 없음
            }
        });

        // 팝업 메시지를 표시
        builder.show();
    }

    private void enableButtons() {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j].setClickable(true);
            }
        }
    }

    private void disableButtons() {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j].setClickable(!isCleared);
            }
        }
    }

    private void ResetPuzzle(){
        // 클리어 상태 초기화
        isCleared = false;

        // 클릭 횟수 초기화
        ClickCount = 0;

        // "횟수: " TextView 업데이트
        TextView countTextView = findViewById(R.id.Count);
        countTextView.setText("횟수: " + ClickCount);

        // 버튼 활성화
        enableButtons();

        // 시작 시간을 현재 시간으로 업데이트하여 초기화
        startTimeMillis = System.currentTimeMillis();

        // 퍼즐 섞기
        shufflePuzzle();
    }

    private void showClearResultPopup(long elapsedSeconds) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("게임 클리어");

        String message = "클리어까지 걸린 시간: " + elapsedSeconds + " 초\n" +
                "클리어까지 누른 횟수: " + ClickCount + " 번";

        builder.setMessage(message);
        builder.setPositiveButton("확인", null); // 아무 동작 없음

        // "Reset" 버튼 추가
        builder.setNeutralButton("Reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ResetPuzzle();
            }
        });

        // "End" 버튼 추가
        builder.setNegativeButton("End", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 종료 버튼을 눌렀을 때 종료 여부를 묻는 팝업 표시
                showHomeConfirmationPopup();
            }
        });

        // 팝업 메시지를 표시
        builder.show();
    }
}