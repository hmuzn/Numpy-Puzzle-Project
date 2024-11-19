package com.example.number_puzzle_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }

    // 게임 시작 버튼 클릭 시 호출되는 메소드
    public void startGame(View view) {
        // Intent를 사용하여 GameActivity로 화면 전환
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    // 게임 방법 버튼 클릭 시 호출되는 메소드
    public void showGameInstructions(View view) {
        showGameInstructionsPopup();
    }

    private void showGameInstructionsPopup() {
        // 레이아웃을 생성하고 초기화
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // ImageView를 생성하고 이미지를 설정
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.how_to_play); // "your_image"는 이미지 리소스의 이름

        // ImageView를 레이아웃에 추가
        layout.addView(imageView);

        // 메시지를 설정
        TextView messageTextView = new TextView(this);
        messageTextView.setText("      1. 숫자 타일을 움직여서 위 그림대로 순서를 맞추세요!");
        layout.addView(messageTextView);

        TextView messageTextView2 = new TextView(this);
        messageTextView2.setText("      2. 빈칸과 근처의 숫자 타일끼리만 바꿀 수 있습니다!");
        layout.addView(messageTextView2);

        // 팝업 창을 생성하고 레이아웃을 설정
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("게임 방법");
        builder.setView(layout); // 레이아웃을 팝업 창에 설정

        // 확인 버튼을 눌렀을 때 아무 동작 없음
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 아무 동작 없음
            }
        });

        // 팝업 메시지를 표시
        builder.show();
    }

    public void showExitConfirmationPopup() {
        // 종료 여부를 묻는 팝업 메시지 표시
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("게임 끄기");
        builder.setMessage("게임을 종료하시겠습니까?");
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
}