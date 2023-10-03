//**************************************************************************//
// Assembler in Vusial Studio.cpp.  Usually a "C" application kicks off in	//
// main(), here we kick off in wmain(), not main() this is a unicode build  //
// (w for "wide").															//
//																			//
// This is a "C" program, and we do the input and output in "C", as doing	//
// so using assembler is a real pain.										//
//**************************************************************************//

//**************************************************************************//
// This code is copyright of Dr Nigel Barlow, lecturer in computing,		//
// University of Plymouth, UK.  email: nigel@soc.plymouth.ac.uk.			//
//																			//
// You may use, modify and distribute this (rather cack-handed in places)	//
// code subject to the following conditions:								//
//																			//
//	1:	You may not use it, or sell it, or use it in any adapted form for	//
//		financial gain, without my written premission.						//
//																			//
//	2:	You must not remove the copyright messages.							//
//																			//
//	3:	You should correct at least 10% of the typig abd spekking errirs.   //
//**************************************************************************//


#include "stdafx.h"
#include <stdlib.h>


void printChar(char c);
void printStr(char *strAddr);
void printInt(int someInt);


//**************************************************************************//
// Define some variables.  Not in assembler yet.							//
//**************************************************************************//
int numItems;
int *items;		// Pointer to the items
int anItem;
int counter;

//**************************************************************************//
// C programs usually kick off in main().  This is a unicode build, so it	//
// kicks off in wmain() (wide main).										//
//**************************************************************************//
int wmain(int argc, _TCHAR* argv[])
{
	items = (int *) malloc(1000); // I thnk that should cover it.

	//**********************************************************************//
	// As usual, we use "C" to do the heavy lifting and enter the numbers.	//
	//**********************************************************************//
	
	numItems = 0;
	do
	{
		printf("Enter item %d (0 means end of data): ", numItems + 1);
		scanf("%d", &anItem);
		items[numItems] = anItem;
		numItems++;

	} while (anItem != 0);
		


	//**********************************************************************//
	// Into assembler.														//
	// All the text in green after the "//" is a comment.  Comments are		//
	// purely for us humans to leave notes for ourselves; they are ignored	//
	// by the computer.														//
	//**********************************************************************//
	__asm
	{
		mov ebx, [numItems]
		sub ebx, 2
		mov eax, 4 
		mul ebx
		inc ebx
		mov [counter], 1

		mov esi, [items]
		add esi, eax

		loop1:	mov ecx, [esi]
				mov edx, [counter]
				cmp edx, 10
				jae skip1
				push 0
				call printInt
				mov ecx, [esi]
		skip1:	push [counter]
				call printInt				
				push '|'
				call printChar
				inc [counter]
				

				call printStars
				sub esi, 4

				mov edx, [counter]
				cmp edx, ebx
				jne loop1

				

		
			

		
		
		
		
		
		
		
		jmp finish		// We need to jump past the subroutine(s) that follow
							// else the CPU will just carry on going.





		//**********************************************************************//
		// Subroutimes start here, bits of code we want to execute more than	//
		// once, or just because we want to split a compliated task into several//
		// simpler ones.														//
		//**********************************************************************//
	printStars:
		mov ecx, [esi]
			 
	another:push ecx
			push '*'
			call printChar
			pop ecx
			dec ecx
			cmp ecx, 0
			jne another
			call printNewLine
			ret

		printNewLine:
			push '\r'				// Two lines to print a char.
			call printChar

			push '\n'				// Two lines to print another char.
			call printChar

			ret					// And back to <whaerever we came from>





		//**********************************************************************//
		// Label to mark the end; do nothing, just jump here to finish.			//
		//**********************************************************************//
		finish:						// Do nothing			
	}


	//**********************************************************************//
	// Out of assembler.													//
	//**********************************************************************//
	printf("press enter to quit\n");
	char dummy[10];	     //Just in case several keys in buffer
	scanf("%c", dummy);  //pause.
	scanf("%c", dummy);  //pause.  And once more.  Something weird going on.
}



//**********************************************************************//
// Prints a single character.											//
// Push the char to be printed onto the stack; a First In Last Out		//
// data structure.  Remember, unlike C#, a char here is 1 byte in size. //
//																		//
// Parameters in: Push a single char onto the stack, as above.			//
// Returns:		  Nothing.												//
// Other issues:  Does it preserve CPU registers eax, ebx, ecx, edx,	//
//				  esi, edi etc.?  No idea (it takes us into "C", so		//
//				  assume not.											//
//**********************************************************************//
void printChar(char c)
{
	printf("%c", c);  //%c means as a char

}	// we don't seee the "ret" instruction unless you view the ".cod" listing 
	// in the "debug" folder.



	//**********************************************************************//
	// Print a whole string, which must end with a zero byte.				//
	// it takes one parameter, which is the start address of the string.	//
	//**********************************************************************//
void printStr(char *strAddr)
{
	printf("%s", strAddr);
}	// we don't seee the "ret" instruction unless you view the ".cod" listing 
	// in the "debug" folder.




//**********************************************************************//
// Prints a single integer	.											//
// Push the integer to be printed onto the stack; a First In Last Out	//
// data structure.  Remember, an integer here is 4 bytes in size.		//
//																		//
// Parameters in: push a single integer onto the stack, as above.		//
// Returns:		  Nothing.												//
// Other issues:  Does it preserve CPU registers eax, ebx, ecx, edx,	//
//				  esi, edi etc.?  No idea (it takes us into "C", so		//
//				  assume not.											//
//**********************************************************************//
void printInt(int someInt)
{
	printf("%d", someInt);
}	// we don't seee the "ret" instruction unless you view the ".cod" listing 
	// in the "debug" folder.


