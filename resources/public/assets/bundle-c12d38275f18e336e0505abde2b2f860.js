document.addEventListener("DOMContentLoaded",function(){document.body.addEventListener("click",function(b){var c=b.target,a=c.getAttribute("data-confirm");a&&(b.preventDefault(),a&&confirm(a)&&c.closest("form").submit())})});