import RPi.GPIO as GPIO
import time

# --- 핀 설정---
# 왼쪽 바퀴
IN1 = 25
IN2 = 8

# 오른쪽 바퀴
IN3 = 1
IN4 = 7

# --- GPIO 초기화 ---
GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)

# 핀들을 출력 모드로 설정
pins = [IN1, IN2, IN3, IN4]
for pin in pins:
    GPIO.setup(pin, GPIO.OUT)
    GPIO.output(pin, GPIO.LOW)

# --- 움직임 함수 정의 ---
def stop():
    print("정지")
    GPIO.output(IN1, 0)
    GPIO.output(IN2, 0)
    GPIO.output(IN3, 0)
    GPIO.output(IN4, 0)

def forward():
    print("앞으로")
    # 왼쪽 전진
    GPIO.output(IN1, 1)
    GPIO.output(IN2, 0)
    # 오른쪽 전진
    GPIO.output(IN3, 1)
    GPIO.output(IN4, 0)

def backward():
    print("뒤로")
    # 왼쪽 후진
    GPIO.output(IN1, 0)
    GPIO.output(IN2, 1)
    # 오른쪽 후진
    GPIO.output(IN3, 0)
    GPIO.output(IN4, 1)

def turn_left():
    print("좌회전")
    # 왼쪽 후진, 오른쪽 전진
    GPIO.output(IN1, 0)
    GPIO.output(IN2, 1)
    GPIO.output(IN3, 1)
    GPIO.output(IN4, 0)

def turn_right():
    print("우회전")
    # 왼쪽 전진, 오른쪽 후진
    GPIO.output(IN1, 1)
    GPIO.output(IN2, 0)
    GPIO.output(IN3, 0)
    GPIO.output(IN4, 1)

# --- 실행 부분 ---
try:
    print("RC카 테스트 (3초 대기)")
    time.sleep(3)

    forward()
    time.sleep(2) # 2초간 전진
    
    stop()
    time.sleep(1)

    backward()
    time.sleep(2) # 2초간 후진

    stop()
    time.sleep(1)

    turn_left()
    time.sleep(2) # 2초간 좌회전

    stop()
    time.sleep(1)
    
    turn_right()
    time.sleep(2) # 2초간 우회전

    stop()
    print("=== 테스트 종료 ===")

except KeyboardInterrupt:
    # Ctrl+C 누르면 즉시 멈춤
    print("종료")
    stop()

finally:
    GPIO.cleanup()
