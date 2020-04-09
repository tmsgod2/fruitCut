package com.sunlab.fruitscut

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    var number:Int = 0

    var firstY: Float = 0F

    // 이미지 갯수 설정
    var fruitnumber : Int = 1

    // 과일 이미지 1번
    val fruitImageId = R.drawable.fruit_image_01

    var currentImageId = 0

    //현재 연습이 진행중인지 확인하는 Boolean 변수
    var isPractice = false

    // 과일 이미지를 담는 큐
    // 터치시 생성된 순서대로 애니메이션을 실행시키기 위해 큐를 사용
    lateinit var imageQueue: Queue<fruitsData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 초기는 연습하지 않으므로 false로 초기화
        isPractice = false

        // 이미지 저장 큐 링크드 리스트로 초기화
        imageQueue = LinkedList()

        // 시작버튼 클릭 이벤트 생성
        startButton.setOnClickListener {
            // 시작하면 기존 연습했던 부분은 전부 날아감
            deleteImage()

            Toast.makeText(this,"연습시작",Toast.LENGTH_SHORT).show()

            // 연습할 횟수에 따라 LinearLayout과 이미지 생성
            // LinearLayout은 연습 5개당 하나씩 증가
            // 한 LinearLayout당 이미지 5개 표시
            createLinearLayout(parentLinear,fruitnumber)
            isPractice = true

            // 연습 시간 측정 시작
        }

        // 연습종료 버튼 클릭 이벤트 생성
        deleteButton.setOnClickListener {
            // LinearLayout과 이미지 모두 제거
           deleteImage()
            Toast.makeText(this,"연습취소",Toast.LENGTH_SHORT).show()
        }

        // 연습량 상승 버튼 클릭 이벤트 생성
        upButton.setOnClickListener {
            //최대 2=100번까지 가능
            if(fruitnumber<100) {
                fruitnumber++
                numberText.setText("$fruitnumber")
            }
        }

        // 연습량 하락 버튼 클릭 이벤트 생성
        downButton.setOnClickListener {
            // 연습량 하락 중 1 이하일 경우 1로 고정
            if(fruitnumber>1){
                fruitnumber--
                numberText.setText("$fruitnumber")
            }
        }

        // 버튼 공간을 제외한 화면 터치시 이벤트
        parentLinear.setOnTouchListener { view: View, motionEvent:MotionEvent ->
            // 터치시 왼쪽 위를 기준으로 오른쪽으로 하나씩 이미지를 바꿈
            // 큐를 이용하여 해당 이미지 사진을 바꿈
            when(motionEvent.action){
                MotionEvent.ACTION_DOWN->{
                    if(isPractice){
                        val currentImage = imageQueue.poll()
                    currentImage.image?.setImageResource(currentImage.imageId+1) == null
                    if(imageQueue.peek()==null){
                        Toast.makeText(this,"연습완료! ",Toast.LENGTH_SHORT).show()
                        deleteImage()
                        }
                    }
                }
            }
            true
        }

        // 이미지 갯수 표시 텍스트의 터치 이벤트
        // 위로 드래그시 추가, 밑으로 드래그시 감소
        numberText.setOnTouchListener { view:View, motionEvent:MotionEvent ->

            var i: Int
            when(motionEvent.action){
                MotionEvent.ACTION_DOWN -> {
                    firstY = motionEvent.y
                    number = numberText.text.toString().toInt()
                    i=0
                }
                MotionEvent.ACTION_MOVE->{
                    if(motionEvent.y<firstY){
                        i = ((motionEvent.y - firstY)/50).toInt()
                        if((number-i)<100)
                            numberText.setText("${number-i}")
                        else
                            numberText.setText("100")
                    }
                    else if(motionEvent.y>firstY){
                        i = ((motionEvent.y - firstY)/50).toInt()
                        if((number-i)>0)
                        numberText.setText("${number-i}")
                        else
                            numberText.setText("1")
                    }
                }
                MotionEvent.ACTION_UP->{
                    fruitnumber = numberText.text.toString().toInt()
                }
            }
            true
        }

        BPMButton.setOnClickListener {
        }

        fruitImageView.setOnTouchListener { view:View, motionEvent:MotionEvent ->
            when(motionEvent.action){
                MotionEvent.ACTION_DOWN -> {
                    if(currentImageId <= 4) currentImageId += 1
                    else currentImageId = 0

                    if(currentImageId == 5) fruitImageView.setImageResource(R.drawable.random_image)
                    else
                    fruitImageView.setImageResource(fruitImageId+currentImageId*2+1)
                }
            }

            true
        }


    }

    // 이미지를 삭제할 시의 메소드
    fun deleteImage(){
        parentLinear.removeAllViews()
        isPractice = false
        // 이미지 큐의 모든 원소가 없어질 때 까지 반복
        while(imageQueue.poll() != null){
        }
    }

    // 이미지를 넣을 LinearLayout 생성
    fun createLinearLayout(parentLayout: LinearLayout, fruitstack:Int){

        // 반복 횟수 설정 (5개당 1개의 LinearLayout)
        var i = (fruitstack/5)+1
        // 5의 배수일 경우 하나 제거
        // ex) 이미지 3 -> 레이아웃 1, 이미지 5 -> 레이아웃 2 사태 방지
        if (fruitstack%5 == 0){
            i--
        }

        // LinearLayout을 담는 부모 Layout의 weightSum 설정
        // 균형을 맞추기 위함
        parentLayout.weightSum = i.toFloat()

        // 이미지 갯수 저장
        var stack = fruitstack

        // 생성될 LinearLayout 파라미터 설정
        val param = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT).apply {
            weight = 1F
        }

        // 레이아웃 갯수만큼 반복
        while(i-->0){
            //레이아웃 생성
            val linearLayout = LinearLayout(this)
            linearLayout.gravity = Gravity.CENTER
            linearLayout.layoutParams = param

            //지정된 갯수(최대 5개) 만큼 이미지 생성 및 추가
            if(stack>=5){
                createImage(linearLayout,0,5)
                stack -= 5
            }
            // 5개 이하라면 나머지 갯수 추가
            else {
                createImage(linearLayout, 0, stack)
            }
            parentLayout.addView(linearLayout)
        }
    }

    // 이미지 생성 메소드
    fun createImage(linearLayout: LinearLayout,fruitnum:Int, stack:Int){

        // 이미지 파라미터 생성
        val imageParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT).apply {
            weight = 1F
        }



        // 이미지 갯수만큼 반복
        var i = 0
        while(i<stack) {
            var tmpImageId = currentImageId*2
            if(currentImageId == 5)
            tmpImageId = (0..4).random() * 2
            // 이미지 객체 생성
            val image = ImageView(this)
            // 이미지 넣기
            image.setImageResource(fruitImageId + tmpImageId)
            image.layoutParams = imageParams
            linearLayout.addView(image)
            // 애니메이션 지정
            image.startAnimation(AnimationUtils.loadAnimation(this@MainActivity,R.anim.rotate))

            // 이미지 큐에 넣기
            imageQueue.add(fruitsData(image,fruitImageId + tmpImageId))
            i++
        }
    }

}

