from random import randint
import math

def makeMatrix(x, y, start, end):
    maatriks = []
    for i in range(y):
        rida = []
        for j in range(x):
            rida.append(randint(0, 1))
        maatriks.append(rida)
    maatriks[start[1]][start[0]] = 2
    maatriks[end[1]][end[0]] = 3
    return maatriks

def printmaatrix(maatriks):
    for i1 in maatriks:
        rida = ""
        for j1 in i1:
            rida += str(j1)
        print(rida)

maatrix = makeMatrix(10, 10, [0, 0], [9, 9])
printmaatrix(maatrix)
openList = [[0, 0, [0, 0], 0, 141, 141]] #[x, y, [parentx, parenty], G, H, F]
openListF = [141]
closedList = []
while len(openList) > 0:
    current = openList.pop(openListF.index(min(openListF)))
    closedList.append(current)
    for i in range(-1, 1):
        for j in range(-1, 1):
            uusx = current[0] + i
            uusy = current[1] + j
            print(uusx, uusy)
            if (uusx) > 0 and (uusy) > 0: #kui ruut asub piirjoontes
                if maatrix[uusy][uusx] == 0: #kui ruut on avatud
                    h = int(math.sqrt(pow(uusx + 1, 2) + pow(uusy + 1, 2)) * 10)
                    g = current[3] + 10
                    f = g + h
                    naaber = [uusx, uusy, [current[0], current[1]], g, h, f]
                    print(naaber)