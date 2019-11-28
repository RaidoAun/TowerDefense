from random import randint
import math

def makeMatrix(x, y, start, end):
    maatriks = []
    for i in range(y):
        rida = []
        for j in range(x):
            rida.append(0)
        maatriks.append(rida)
    maatriks[3][3] = 1
    maatriks[3][4] = 1
    maatriks[3][2] = 1
    maatriks[3][1] = 1
    maatriks[1][0] = 1
    maatriks[1][1] = 1
    maatriks[1][2] = 1
    maatriks[start[1]][start[0]] = 2
    maatriks[end[1]][end[0]] = 3
    return maatriks

def printmaatrix(maatriks):
    for i1 in maatriks:
        rida = ""
        for j1 in i1:
            rida += str(j1)
        print(rida)

def avatudnaabrid():
    naabrid = []
    for i in range(-1, 2, 2):
        for j in range(2):
            if j == 0:
                uusx = current[0] + i
                uusy = current[1]
            else:
                uusx = current[0]
                uusy = current[1] + i
            if (uusx >= 0 and uusx <= len(maatrix[0]) - 1) and (uusy >= 0 and uusy <= len(maatrix) - 1): #kui ruut asub piirjoontes
                if maatrix[uusy][uusx] == 0 or maatrix[uusy][uusx] == 3: #kui ruut on avatud
                    h = int(math.sqrt(pow(end[0] - uusx, 2) + pow(end[1] - uusy, 2)) * 10)
                    g = current[3] + 10
                    f = g + h
                    naaber = [uusx, uusy, [current[0], current[1]], g, h, f]
                    if [uusx, uusy] not in closedListxy:
                        naabrid.append(naaber)
    return naabrid

def lisanaabridopenlisti():
    for n in avatud_naabrid:
        openList.append(n)
        openListF.append(n[5])

def leiatee():
    tee = [end]
    eelviimane = closedList[-1][2]
    closedListxy.remove(end)
    closedList.pop(-1)
    while True:
        viimane = eelviimane
        eelviimane = closedList[closedListxy.index(viimane)][2]
        tee.append(viimane)
        closedList.pop(closedListxy.index(viimane))
        closedListxy.remove(viimane)
        if eelviimane == start:
            tee.append(eelviimane)
            break
    return tee

def märgistatee():
    for i in tee:
        if i != start and i != end:
            maatrix[i[1]][i[0]] = "♥"

leitud = False
start = [0, 0]
end = [20, 7]
maatrix = makeMatrix(40, 10, start, end)
halgus = int(math.sqrt(pow(end[0] -start[0], 2) + pow(end[1] - start[1], 2)) * 10)
openList = [[start[0], start[1], start, 0, halgus, halgus]] #[x, y, [parentx, parenty], G, H, F]
openListF = [141]
closedList = []
closedListxy = []

while len(openList) > 0:
    #print(openList)
    #print(openListF.index(min(openListF)))
    #print(current)
    #print(openListF)
    while True:
        current = openList.pop(openListF.index(min(openListF)))
        openListF.remove(min(openListF))
        if [current[0], current[1]] not in closedListxy:
            break
    closedList.append(current)
    closedListxy.append([current[0], current[1]])
    if [current[0], current[1]] == end: #kui lõpp on leitud
        print("HEUREKA!")
        #print(closedList)
        leitud = True
        tee = leiatee()
        märgistatee()
        print(tee)
        printmaatrix(maatrix)
        break
    avatud_naabrid = avatudnaabrid()
    if len(avatud_naabrid) > 0:
        lisanaabridopenlisti()

if not leitud:
    print("Lõppu pole võimalik jõuda")