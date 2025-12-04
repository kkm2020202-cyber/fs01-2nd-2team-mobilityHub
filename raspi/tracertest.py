# -*- coding: utf-8 -*-

# 라즈베리파이 GPIO 패키지 
import RPi.GPIO as GPIO
from time import sleep

# 모터 상태
STOP  = 0
FORWARD  = 1
BACKWORD = 2

# 모터 채널
CH1 = 0  # 오른쪽
CH2 = 1  # 왼쪽

# PIN 입출력 설정
OUTPUT = 1
INPUT = 0

# PIN 설정
HIGH = 1
LOW = 0

# =========================
# 모터 핀 정의
# =========================
# PWM PIN
ENA = 26 
ENB = 0  

# GPIO PIN
IN1 = 25
IN2 = 8  
IN3 = 1 
IN4 = 7  

# =========================
# 3채널 라인트레이서 모듈 핀
# =========================
LS_LEFT   = 5   # 왼쪽 센서 (BCM 5 예시)
LS_CENTER = 6   # 중앙 센서 (BCM 6 예시)
LS_RIGHT  = 13  # 오른쪽 센서 (BCM 13 예시)


# - 검은 선(라인) 위에서 출력 LOW(0)
# - 흰 바닥에서 HIGH(1)
LINE  = 0   # 라인(검은색) 감지
SPACE = 1   # 바닥(라인 없음)

# 모터 기본 속도 (PWM Duty, 0~100)
RIGHT_BASE_SPEED = 40   # 오른쪽 바퀴
LEFT_BASE_SPEED  = 40   # 왼쪽 바퀴

# 필요하면 좌우 힘 보정 (예: 오른쪽이 약하면 45, 왼쪽 40 이런 식으로)
# RIGHT_BASE_SPEED = 45
# LEFT_BASE_SPEED  = 40

# 핀 설정 함수
def setPinConfig(EN, INA, INB):        
    GPIO.setup(EN, GPIO.OUT)
    GPIO.setup(INA, GPIO.OUT)
    GPIO.setup(INB, GPIO.OUT)
    # 100khz 로 PWM 동작 시킴 
    pwm = GPIO.PWM(EN, 100) 
    pwm.start(0)  # 처음에는 정지
    return pwm

# 모터 제어 함수
def setMotorContorl(pwm, INA, INB, speed, stat):
    # 모터 속도 제어 PWM
    pwm.ChangeDutyCycle(speed)  
    
    if stat == FORWARD:
        GPIO.output(INA, HIGH)
        GPIO.output(INB, LOW)
        
    elif stat == BACKWORD:
        GPIO.output(INA, LOW)
        GPIO.output(INB, HIGH)
        
    elif stat == STOP:
        GPIO.output(INA, LOW)
        GPIO.output(INB, LOW)

# 모터 제어함수 간단하게 사용하기 위해 한번더 래핑
def setMotor(ch, speed, stat):
    if ch == CH1:
        # pwmA는 핀 설정 후 pwm 핸들을 리턴 받은 값
        setMotorContorl(pwmA, IN1, IN2, speed, stat)
    else:
        # pwmB는 핀 설정 후 pwm 핸들을 리턴 받은 값
        setMotorContorl(pwmB, IN3, IN4, speed, stat)

# 라인트레이서 센서 설정
def setup_line_tracer():
    GPIO.setup(LS_LEFT, GPIO.IN)
    GPIO.setup(LS_CENTER, GPIO.IN)
    GPIO.setup(LS_RIGHT, GPIO.IN)

# 센서 값 읽기 (세 채널 한 번에)
def read_line_sensors():
    left   = GPIO.input(LS_LEFT)
    center = GPIO.input(LS_CENTER)
    right  = GPIO.input(LS_RIGHT)
    return left, center, right

# 라인트레이싱 메인 로직
def line_trace_loop():
    while True:
        left, center, right = read_line_sensors()
        # 디버깅용으로 보고 싶으면 주석 해제
        # print(f"L={left}, C={center}, R={right}")

        # 1) 중앙만 라인: 직진
        if center == LINE and left == SPACE and right == SPACE:
            setMotor(CH1, RIGHT_BASE_SPEED, FORWARD)
            setMotor(CH2, LEFT_BASE_SPEED, FORWARD)

        # 2) 왼쪽 센서 쪽이 라인: 차가 오른쪽으로 치우친 상태 → 왼쪽으로 돌려줘야 함
        elif left == LINE and center == LINE and right == SPACE:
            # 살짝 왼쪽 보정 (왼쪽 바퀴만 조금 느리게)
            setMotor(CH1, RIGHT_BASE_SPEED, FORWARD)
            setMotor(CH2, int(LEFT_BASE_SPEED * 0.6), FORWARD)

        elif left == LINE and center == SPACE and right == SPACE:
            # 강하게 왼쪽 회전 (왼쪽 바퀴 정지, 오른쪽만 전진)
            setMotor(CH1, RIGHT_BASE_SPEED, FORWARD)
            setMotor(CH2, 0, STOP)

        # 3) 오른쪽 센서 쪽이 라인: 차가 왼쪽으로 치우친 상태 → 오른쪽으로 돌려줘야 함
        elif right == LINE and center == LINE and left == SPACE:
            # 살짝 오른쪽 보정
            setMotor(CH1, int(RIGHT_BASE_SPEED * 0.6), FORWARD)
            setMotor(CH2, LEFT_BASE_SPEED, FORWARD)

        elif right == LINE and center == SPACE and left == SPACE:
            # 강하게 오른쪽 회전
            setMotor(CH1, 0, STOP)
            setMotor(CH2, LEFT_BASE_SPEED, FORWARD)

        # 4) 세 개 다 라인 or 세 개 다 바닥 → 교차로나 선 이탈로 보고 일단 정지
        else:
            setMotor(CH1, 0, STOP)
            setMotor(CH2, 0, STOP)

        sleep(0.01)  # 루프 너무 빠르지 않게

# =========================
# 메인 실행부
# =========================
if __name__ == "__main__":
    GPIO.setmode(GPIO.BCM)
    GPIO.setwarnings(False)

    # 모터 핀 설정
    pwmA = setPinConfig(ENA, IN1, IN2)
    pwmB = setPinConfig(ENB, IN3, IN4)

    # 라인트레이서 센서 설정
    setup_line_tracer()

    print("3채널 전방 라인트레이서 라인 트레이싱 시작")
    sleep(1)

    try:
        line_trace_loop()
    except KeyboardInterrupt:
        print("사용자 종료")
    finally:
        # 정지 후 GPIO 정리
        setMotor(CH1, 0, STOP)
        setMotor(CH2, 0, STOP)
        GPIO.cleanup()
