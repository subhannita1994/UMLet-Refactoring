// Namespace Declaration
using System;

// helper class
class OutputClass
{
    private string myString;
    
    public string otherString;

    // Constructor
    public OutputClass(string inputString)
    {
        myString = inputString;
        string myString2;
        int varInConstructor = 0;
    }

    // Instance Method
    public void printString()
    {
    	string myString3;
        int varInConstructor2 = 0;
        Console.WriteLine("{0}", myString);
    }

    // Destructor
    ~OutputClass()
    {
        // Some resource cleanup routines
    }
}