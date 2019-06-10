$(document).ready(function () {
    let url = new URL(window.location.toString());
    let comParams = url.searchParams.get('curLang');

    let a = document.getElementsByTagName('a');
    for(let i = 0; i < a.length; i++) {
        if(!a[i].getAttribute('class').toString().includes("carousel") && !a[i].getAttribute('class').toString().includes("non")){

            let newUrl = new URL(window.location.origin + a[i].getAttribute('href').toString());
            if(comParams != null){
                newUrl.searchParams.set('curLang', comParams);
            }
            a[i].setAttribute('href', newUrl.toString());
        }
    }
});