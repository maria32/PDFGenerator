/**
 * Created by martanase on 3/28/2017.
 */
//output format: https://msdn.microsoft.com/en-us/library/office/ff746500.aspx
var fso = new ActiveXObject("Scripting.FileSystemObject");
var pptPath = WScript.Arguments(0);
var pptParentFolder = fso.getParentFolderName(pptPath);
pptPath = fso.GetAbsolutePathName(pptPath);

var pdfPath = pptParentFolder + "/temp/" + fso.getFileName(pptPath).replace(/\.ppt[^.]*$/, ".pdf");
var objWord = null;

try
{
    WScript.Echo("Saving '" + pptPath + "' as '" + pdfPath + "'...");

    objWord = new ActiveXObject("PowerPoint.Application");
    //objWord.Visible = false;

    var objDoc = objWord.Presentations.Open(pptPath);

    var wdFormatPdf = 32;
    objDoc.SaveAs(pdfPath, wdFormatPdf);
    objDoc.Close();

    WScript.Echo("Done.");
}
finally
{
    if (objWord != null)
    {
        objWord.Quit();
    }
}