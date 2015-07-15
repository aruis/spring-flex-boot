/**
 * Created by liurui on 15/7/15.
 */
function initTable() {
    var data = [
        ["", "Ford", "Volvo", "Toyota", "Honda"],
        ["2014", 10, 11, 12, 13],
        ["2015", 20, 11, 14, 13],
        ["2016", 30, 15, 12, 13]
    ];

    var container = $('#example')[0];
    var hot = new Handsontable(container, {
        data: data,
        minSpareRows: 1,
        rowHeaders: true,
        colHeaders: true,
        contextMenu: true
    });
}

function printTable() {
    var oldDocumentBody;
    oldDocumentBody = document.body.innerHTML;
    var item = $("#example")[0];

    html2canvas(item, {
        onrendered: function (canvas_source) {
            var numImage = 0;

            var pasWidth = canvas_source.width;
            var pasHeight = canvas_source.height;

            // Reinitialisation de la page courante
            document.body.innerHTML = "<html><head><title></title></head><body></body>";

            // Creation d'un canvas tampon
            var canvas_tampon = document.createElement("canvas");
            canvas_tampon.height = pasHeight;
            canvas_tampon.width = pasWidth;
            var canvas_tampon_context = canvas_tampon.getContext('2d');

            // Parcours du canvas_source dans la hauteur
            var heightRestantAImprimer = canvas_source.height;
            var numLigne = 1;
            while (heightRestantAImprimer > 0) {

                // Parcours du canvas_source dans la largeur
                var widthRestantAImprimer = canvas_source.width;
                var numColonne = 1;
                while (widthRestantAImprimer > 0) {
                    numImage++;

                    // Export d'une partie du canvas_source dans le canvas_tampon
                    canvas_tampon_context.drawImage(canvas_source, ((numColonne - 1) * pasWidth), ((numLigne - 1) * pasHeight), pasWidth, pasHeight, 0, 0, pasWidth, pasHeight);

                    // Creation d'une image afin de contenir la découpe
                    var img = new Image;
                    img.id = 'tempPrintImage' + numImage;
                    img.src = canvas_tampon.toDataURL();

                    // Ajout de l'image à la page courante
                    document.body.appendChild(img);

                    // Passage à la colonne suivante
                    widthRestantAImprimer = widthRestantAImprimer - pasWidth;
                    numColonne++;
                }

                // Passage à la ligne suivante
                heightRestantAImprimer = heightRestantAImprimer - pasHeight;
                numLigne++;
            }

            // Print Document
            window.print();

            // Restore document
            document.body.innerHTML = oldDocumentBody;

            // Clean memory
            oldDocumentBody = "";
        }
    });
}

$(document).ready(function () {
    initTable();
});