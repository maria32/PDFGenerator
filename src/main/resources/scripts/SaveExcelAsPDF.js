var fso = new ActiveXObject("Scripting.FileSystemObject");
var excelPath = WScript.Arguments(0);
var excelParentFolder = fso.GetParentFolderName(excelPath);
excelPath = fso.GetAbsolutePathName(excelPath);

var pdfPath = excelParentFolder + "/temp/" + fso.getFileName(excelPath).replace(/\.xls[^.]*$/, ".pdf");
var objExcel = null;

try
{
    WScript.Echo("Saving '" + excelPath + "' as '" + pdfPath + "'...");

    objExcel = new ActiveXObject("Excel.Application");
    objExcel.Visible = false;

    var objWorkbook = objExcel.Workbooks.Open(excelPath);

	var wdFormatPdf = 57;
	objWorkbook.SaveAs(pdfPath, wdFormatPdf);
	objWorkbook.Close();

    WScript.Echo("Done.");
}
finally
{
    if (objExcel != null)
    {
        objExcel.Quit();
    }
}