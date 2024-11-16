import future
from random import randint
import _thread
driving = True
drunkness = 2
happiness = 9
if not driving:
  screen.loadPng('alcogotchi.png')

  
  screen.text("Drunkness", x=10, y=90, ext=1, color=255)
  screen.rect(10,100,drunkness*15,10,(500-50*drunkness, 50*drunkness, 555),1)
  
  screen.text("Happiness", x=10, y=110, ext=1, color=255)
  
  screen.rect(10,120,happiness*15,10,(500-50*happiness, 50*happiness, 555),1)
lane = 0
# opposing_vehicle_y = 0

base_speed = 6*drunkness
range_speed = 30

opposing_vehicle_ys = [0,0,0]
number_of_vehicles = 10
opposing_vehicle_speeds = [randint(base_speed,base_speed+range_speed) for i in range(number_of_vehicles)]
opposing_vehicle_lanes = [randint(0,3) for i in range(number_of_vehicles)]
playing = True
startup_time = 0
for i in range(len(opposing_vehicle_ys)):
  screen.rect(13+opposing_vehicle_lanes[i]*40, opposing_vehicle_ys[i],20,40,(500, 500, 555),1)
def play_music(CHASE):
  while True:
    buzzer.melody(CHASE)
CHASE = "a4:1 b c5 b4 a:2 r a:1 b c5 b4 a:2 r a:2 e5 d# e f e d# e b4:1 c5 d c b4:2 r b:1 c5 d c b4:2 r b:2 e5 d# e f e d# e "
  


# new_thread = _thread.start_new_thread(play_music, [CHASE])

while playing and driving:
  
  
  # WAWA = "e3:3 r:1 d#:3 r:1 d:4 r:1 c#:8 "

  startup_time += 1
  if startup_time >= 3:
    for i in range(len(opposing_vehicle_ys)):
      opposing_vehicle_ys[i]+=opposing_vehicle_speeds[i]
      if  80 <= (opposing_vehicle_ys[i]+40) <= 120  and opposing_vehicle_lanes[i] == lane:
        print("GAME OVER")
        playing = False
      if opposing_vehicle_ys[i] > 180:
        opposing_vehicle_ys[i] = 0
        opposing_vehicle_lanes[i] = randint(0,3)
      screen.rect(13+opposing_vehicle_lanes[i]*40, opposing_vehicle_ys[i]-opposing_vehicle_speeds[i],20,40,(0, 0, 555),1)
  
      screen.rect(13+opposing_vehicle_lanes[i]*40, opposing_vehicle_ys[i],20,40,(500, 500, 555),1)
      
  for i in range(10):
    screen.rect(80,i*15,5,10,(500-50*i, 50*i, 555),1)
  for i in range(10):
    screen.rect(40,i*15,5,10,(500-50*i, 50*i, 555),1)
  for i in range(10):
    screen.rect(120,i*15,5,10,(500-50*i, 50*i, 555),1)
  if sensor.btnValue("a"):
    lane -= 1
    if lane < 0:
      lane = 0
  if sensor.btnValue("b"):
    lane += 1
    if lane > 3:
      lane = 3

  if lane == 0:
    screen.rect(13,80,20,40,(500, 500, 555),1)
    screen.rect(53,80,20,40,(0, 0, 0),1)
    screen.rect(93,80,20,40,(0, 0, 0),1)
    screen.rect(133,80,20,40,(0, 0, 0),1)
  if lane == 1:
    screen.rect(53,80,20,40,(500, 500, 555),1)
    screen.rect(13,80,20,40,(0, 0, 0),1)
    screen.rect(93,80,20,40,(0, 0, 0),1)
    screen.rect(133,80,20,40,(0, 0, 0),1)
  if lane == 2:
    screen.rect(93,80,20,40,(500, 500, 555),1)
    screen.rect(33,80,20,40,(0, 0, 0),1)
    screen.rect(53,80,20,40,(0, 0, 0),1)
    screen.rect(133,80,20,40,(0, 0, 0),1)
  if lane == 3:
    screen.rect(133,80,20,40,(500, 500, 555),1)
    screen.rect(13,80,20,40,(0, 0, 0),1)
    screen.rect(53,80,20,40,(0, 0, 0),1)
    screen.rect(93,80,20,40,(0, 0, 0),1)

  screen.refresh()
screen.text("GAME OVER", x=10, y=90, ext=1, color=255)
