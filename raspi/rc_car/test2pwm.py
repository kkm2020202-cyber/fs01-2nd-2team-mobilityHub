import RPi.GPIO as GPIO, time
GPIO.setmode(GPIO.BCM); GPIO.setwarnings(False)
ENA, ENB, IN3, IN4 = 12, 13, 23, 24  # ENA/ENB는 1) 결과에 따라 바꿔서 테스트
GPIO.setup(ENB, GPIO.OUT); GPIO.setup(IN3, GPIO.OUT); GPIO.setup(IN4, GPIO.OUT)
p = GPIO.PWM(ENB, 100); p.start(0)
try:
    GPIO.output(IN3, 1); GPIO.output(IN4, 0)  # 한 방향
    p.ChangeDutyCycle(70); time.sleep(2)
    GPIO.output(IN3, 0); GPIO.output(IN4, 1)  # 반대 방향
    time.sleep(2)
finally:
    p.ChangeDutyCycle(0); GPIO.cleanup()