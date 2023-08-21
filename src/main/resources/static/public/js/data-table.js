$(document).ready(function () {
  $('#data-table').DataTable({
    "scrollY": "60vh",
    "scrollX": true,
    "scrollCollapse": true,
    "aLengthMenu": [[1, 2, 5], [1, 2, 5]],
    "iDisplayLength": 5
  });
  $('.dataTables_length').addClass('bs-select');
});