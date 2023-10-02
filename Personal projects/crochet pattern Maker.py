import bpy
from math import ceil, floor,pi,sin,cos

pattern = ""
symbols = {
    "start": "@",
    "dc":"+",
    "ch":"c",
    "slst":".",
    "htc":"T",
    "tc":"t",
    "2dcst":"V",
    "2dctog":"^"
}
symbolValue = {
    "dc":1,
    "2dcst":2,
    "^":1
}
    

def repeat(input):
    output = []
    if type(input[-1]) is not int:
        repeats = 1
    else:
        repeats = input[-1]
        input = input[:-1]
    for i in range(repeats):
        for j in input:
            if type(j) is list:
                output.extend(repeat(j))
            else:
                if symbols[j] == "V":
                        output.extend(symbols[j])
                        output.extend(symbols[j])
                else:

                    output.extend(symbols[j])
    return output

def makePattern(instructions):
    pattern = [["+"]]
    #maxlength = 0
    for instruction in instructions:
        round = repeat(instruction)
        pattern.append(round)
    return pattern 
def makeObj(pattern):
    with open("obj.obj","w") as file:
        file.write("o Object \n")
        vertexCount = 0
        for y, value in enumerate(pattern):
            r = len(pattern[y])/(2*pi)           
            for x,value2 in enumerate(value):
                if y != 0:
                    newx = cos(x/r)*r
                    newz = sin(x/r)*r
                    vector = "v " + str(newx) + " " + str(y-1) +" "+ str(newz) + "\n"                    
                    file.write(vector)
                else:
                    newx = 0
                    newz = 0
                    vector = "v " + str(newx) + " " + str(y) +" "+ str(newz) + "\n"
                    file.write(vector)
        index = 1
        lines = []
        vertexID = 0

        vertexes = [[]]


        for i, value in enumerate(pattern):            
            for j, value2 in enumerate(value):
                vertexID+=1
                vertexes[i].append(vertexID)
                vertex2 = vertexID + 1
                #if j +1 == len(pattern[i]):
                #    vertex2 = vertexID - len(pattern[i])+1
                file.write("l " + str(vertexID) + " " + str(vertex2) + "\n")    
            vertexes.append([])
                
                
        skip = False
        for i, value in enumerate(reversed(pattern)):
            offset = 0
            it = len(pattern) - i -1
            vertexID -= len(pattern[it])
            if it != 0:
                for j, value2 in enumerate(value):
                    if skip:
                        skip = False
                        vertexID+=1
                    else:                    
                        vertexID+=1
                        match value2:
                            case "+":
                                file.write("l " + str(vertexID) + " " + str(vertexID-len(pattern[it-1])+offset) + "\n")    
                            case "^":
                                file.write("l " + str(vertexID) + " " + str(vertexID-len(pattern[it-1])+offset) + "\n")
                                file.write("l " + str(vertexID) + " " + str(vertexID-len(pattern[it-1])+offset+1) + "\n")   
                                offset+=1
                            case "V":
                                file.write("l " + str(vertexID) + " " + str(vertexID-len(pattern[it-1])+offset) + "\n")
                                file.write("l " + str(vertexID+1) + " " + str(vertexID-len(pattern[it-1])+offset) + "\n")
                                skip = True
                                offset -=1
            vertexID -= len(pattern[it])   
            
instructionss = [
        ["dc",6],
        ["2dcst",6],
        ["dc",12],
        ["dc",12],
        ["dc",12],
        ["2dctog",["dc",10]],
        ["2dctog",["dc",9]],
        ["2dctog",["dc",8]],
        ["2dctog",["dc",7]],
        ["2dctog",["dc",6]],
        ["2dctog",["dc",5]],
        [["2dcst","dc"],3],
        ["dc",9],
        [["2dctog","dc"],3],
        ] 
        
pattern = makePattern(instructionss)
for i , round in enumerate(reversed(pattern)):
    print("\n")
    for j, value in enumerate(round):
        print(value,end="")
    
makeObj(pattern)


#blender stuff
#clear scene, make mesh
bpy.ops.object.mode_set(mode = 'OBJECT')
bpy.ops.object.select_all(action='SELECT')
bpy.ops.object.delete(use_global=False)

#import obj
file_loc = "obj.obj"
imported_object = bpy.ops.import_scene.obj(filepath=file_loc)
obj_object = bpy.context.selected_objects[0]
print('Imported name: ', obj_object.name)
